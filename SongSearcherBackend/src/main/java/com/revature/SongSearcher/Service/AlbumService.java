package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.AlbumDTO;
import com.revature.SongSearcher.Controller.AlbumWOIDDTO;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Repository.AlbumRepository;
import com.revature.SongSearcher.Repository.ArtistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository repo;
    private final ArtistRepository artistRepo;

    public AlbumService(AlbumRepository repo, ArtistRepository artistRepo) {
        this.repo = repo;
        this.artistRepo = artistRepo;
    }

    public List<AlbumDTO> getAll() {
        return repo.findAll().stream()
                .map(a -> new AlbumDTO(a.getId(), a.getTitle(), a.getReleaseYear(), a.getArtist().getId()))
                .toList();
    }

    public AlbumDTO getById(String id) {
        Album album = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new AlbumDTO(album.getId(), album.getTitle(), album.getReleaseYear(), album.getArtist().getId());
    }

    public AlbumDTO create(AlbumWOIDDTO dto) {
        Artist artist = artistRepo.findById(dto.artistId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Album album = new Album();
        album.setTitle(dto.title());
        album.setReleaseYear(dto.releaseYear());
        album.setArtist(artist);

        return new AlbumDTO(repo.save(album).getId(), dto.title(), dto.releaseYear(), artist.getId());
    }

    // working here!!!

    public AlbumDTO update(String id, AlbumDTO dto) {
        Album album = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

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
        album.setTitle(dto.title());
        album.setRelease_year(dto.releaseYear());

        album.getArtists().clear();

        dto.artists().stream()
                .map(this::DTOToArtist)
                .forEach(a -> {
                    album.getArtists().add(a);
                    a.getAlbums().add(album);   // maintain bi-directional relationship
                });

        Album saved = repo.save(album);

        return AlbumToDTO(saved);
    }

    public AlbumDTO patch(String id, AlbumDTO dto) {

        Album album = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.title() != null) {
            album.setTitle(dto.title());
        }

        if (dto.releaseYear() != 0) {
            album.setRelease_year(dto.releaseYear());
        }

        if (dto.artists() != null && !dto.artists().isEmpty()) {

            album.getArtists().clear();

            dto.artists().stream()
                    .map(this::DTOToArtist)
                    .forEach(a -> {
                        album.getArtists().add(a);
                        a.getAlbums().add(album);
                    });
        }

        Album saved = repo.save(album);

        return AlbumToDTO(saved);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}

