package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.AlbumDTO;
import com.revature.SongSearcher.Controller.AlbumWOIDDTO;
import com.revature.SongSearcher.Controller.ArtistDTO;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Repository.AlbumRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository repo;
    //private final ArtistRepository artistRepo;

    public AlbumService(AlbumRepository repo) {
        this.repo = repo;
        //this.artistRepo = artistRepo;
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

    public List<AlbumDTO> getAll() {
        return repo.findAll().stream()
                .map(a -> new AlbumDTO(a.getAlbumId(), a.getTitle(), a.getRelease_year(), a.getArtists().stream().map(this::ArtistToDTO).toList()))
                .toList();
    }

    public AlbumDTO getById(String id) {
        Album album = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new AlbumDTO(album.getAlbumId(), album.getTitle(), album.getRelease_year(), album.getArtists().stream().map(this::ArtistToDTO).toList());
    }

    public AlbumDTO create(AlbumWOIDDTO dto) {

        Album entity = new Album(dto.title(), dto.releaseYear(), dto.artists().stream().map(this::DTOToArtist).collect(Collectors.toSet()));

        for (ArtistDTO a : dto.artists()) {
            Artist artist = DTOToArtist(a);
            artist.getAlbums().add(entity); //add cross compat
        }

        return AlbumToDTO(repo.save(entity));
    }

    // working here!!!

//    public AlbumDTO update(String id, AlbumDTO dto) {
//        Album album = repo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        Artist artist = artistRepo.findById(dto.artistId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        album.setTitle(dto.title());
//        album.setReleaseYear(dto.releaseYear());
//        album.setArtist(artist);
//
//        repo.save(album);
//
//        return dto;
//    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}

