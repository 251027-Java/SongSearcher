package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.*;
import com.revature.SongSearcher.Repository.AlbumRepository;
import com.revature.SongSearcher.Utils.IEmbedder;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Model.Song;
import com.revature.SongSearcher.Repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {

    private final SongRepository repo;
    private final AlbumRepository albumRepo;
    private final IEmbedder embedder;
    private final PlaylistService playlistService;

    public SongService(SongRepository repo, IEmbedder embedder,
                       PlaylistService playlistService,
                       AlbumRepository albumRepo) {
        this.repo = repo;
        this.albumRepo = albumRepo;
        this.embedder = embedder;
        this.playlistService = playlistService;
    }

    private ArtistDTO ArtistToDTO(Artist artist ) {
        return new ArtistDTO(artist.getArtistId(), artist.getName());
    }
    private Artist DTOToArtist ( ArtistDTO dto ) {
        return new Artist(dto.id(), dto.name());
    }
    private AlbumDTO AlbumToDTO (Album album) {
        return new AlbumDTO(album.getAlbumId(), album.getTitle(), album.getRelease_year(), album.getArtists().stream().map(this::ArtistToDTO).toList());
    }
    private AlbumSlimDTO AlbumToSlimDTO (Album album ) {
        return new AlbumSlimDTO(
                album.getAlbumId(),
                album.getTitle(),
                album.getRelease_year()
        );
    }
    private Album DTOToAlbum (AlbumSlimDTO dto) {
        return albumRepo.findById(dto.id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find album"));
    }
    private SongDTO SongToDTO (Song song) {

        List<ArtistDTO> artists  = new ArrayList<>(new ArrayList<>(song.getAlbum().getArtists()).stream().map(this::ArtistToDTO).toList());
        List<ArtistDTO> secondaries = new ArrayList<>(song.getArtists()).stream().map(this::ArtistToDTO).toList();
        artists.addAll(secondaries);

        return new SongDTO(song.getSongId(), song.getTitle(), song.getLength(),
                song.getLyrics(),
                AlbumToSlimDTO(song.getAlbum()),
                artists);
    }
    private Song DTOToSong (SongWOIDDTO dto) {
        return new Song(dto.title(), dto.length(),
                DTOToAlbum(dto.album()),
                dto.artists().stream().map(this::DTOToArtist).collect(Collectors.toSet()),
                dto.lyrics(),
                this.embedder.getEmbedding(dto.lyrics()));
    }
    private Song DTOToSong (SongDTO dto) {
        return new Song(dto.id(), dto.title(), dto.length(),
                DTOToAlbum(dto.album()),
                dto.artists().stream().map(this::DTOToArtist).collect(Collectors.toSet()),
                dto.lyrics(),
                this.embedder.getEmbedding(dto.lyrics()));
    }

    public List<SongDTO> getAll() {

        return repo.findAll()
                .stream()
                .map(this::SongToDTO)
                .toList();
    }

    public SongDTO getById(String id) {
        Song song = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return SongToDTO(song);
    }

    public SongDTO create(SongWOIDDTO dto) {

        Song song = DTOToSong(dto);

        song.getAlbum().getAlbumSongs().add(song);

        for (Artist a : song.getArtists()) {
            a.getSongs().add(song);
        }

        return SongToDTO(this.repo.save(song));

        }

//    public SongDTO update(String id, SongDTO dto) {
//        Song song = repo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        Album album = albumRepo.findById(dto.albumId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        song.setTitle(dto.title());
//        song.setLength(dto.length());
//        song.setLyrics(dto.lyrics());
//        song.setAlbum(album);
//
//        repo.save(song);
//
//        return dto;
//    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public List<SongDTO> searchByLyrics (SearchDTO dto) {
        List<SongDTO> similarSongs = repo.findMostSimilar(embedder.getEmbedding(dto.lyrics()), 10)
                .stream().map(this::SongToDTO).toList();

        return similarSongs;
    }

    public List<SongDTO> getUserSongRecommendations(Long userid) {
        PlaylistDTO favorites = playlistService.getByUserIdAndName(userid, "Favorites");

        List<Song> songs = favorites.songs().stream().map(this::DTOToSong).toList();

        float[] centroidEmbedding = this.computeCentroid(songs.stream().map(Song::getEmbedding).toList());

        List<String> ids = songs.stream().map(Song::getSongId).toList();

        return repo.recommend(centroidEmbedding, ids, 10)
                .stream().map(this::SongToDTO).toList();
    }



    private float[] computeCentroid(List<float[]> vectors) {
        if (vectors == null || vectors.isEmpty()) {
            throw new IllegalArgumentException("Vector list cannot be empty");
        }

        int dim = vectors.get(0).length;
        float[] centroid = new float[dim];

        // Initialize sum array
        for (int i = 0; i < dim; i++) {
            centroid[i] = 0f;
        }

        // Accumulate
        for (float[] vec : vectors) {
            if (vec.length != dim) {
                throw new IllegalArgumentException("All vectors must have the same length");
            }
            for (int i = 0; i < dim; i++) {
                centroid[i] += vec[i];
            }
        }

        // Divide by count
        int count = vectors.size();
        for (int i = 0; i < dim; i++) {
            centroid[i] /= count;
        }

        return centroid;
    }
}

