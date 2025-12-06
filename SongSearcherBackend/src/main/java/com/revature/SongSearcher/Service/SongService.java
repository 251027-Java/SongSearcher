package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.*;
import com.revature.SongSearcher.Utils.IEmbedder;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Model.Song;
import com.revature.SongSearcher.Repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {

    private final SongRepository repo;
    //private final AlbumRepository albumRepo;
    private final IEmbedder embedder;

    public SongService(SongRepository repo, IEmbedder embedder) {
        this.repo = repo;
        //this.albumRepo = albumRepo;
        this.embedder = embedder;
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
    private AlbumSlimDTO AlbumToSlimDTO ( Album album ) {
        return new AlbumSlimDTO(
                album.getAlbumId(),
                album.getTitle(),
                album.getRelease_year()
        );
    }
    private Album DTOToAlbum (AlbumDTO dto) {
        return new Album(dto.id(), dto.title(), dto.releaseYear(),
                dto.artists().stream().map(this::DTOToArtist).collect(Collectors.toSet()));
    }
    private SongDTO SongToDTO (Song song) {
        return new SongDTO(song.getSongId(), song.getTitle(), song.getLength(),
                song.getLyrics(),
                AlbumToSlimDTO(song.getAlbum()),
                new ArrayList<>(song.getArtists()).stream().map(this::ArtistToDTO).toList());
    }
    private Song DTOToSong (SongWOIDDTO dto) {
        return new Song(dto.title(), dto.length(),
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
}

