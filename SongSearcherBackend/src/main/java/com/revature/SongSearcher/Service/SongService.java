package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.SongDTO;
import com.revature.SongSearcher.Controller.SongWOIDDTO;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Song;
import com.revature.SongSearcher.Repository.AlbumRepository;
import com.revature.SongSearcher.Repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SongService {

    private final SongRepository repo;
    private final AlbumRepository albumRepo;

    public SongService(SongRepository repo, AlbumRepository albumRepo) {
        this.repo = repo;
        this.albumRepo = albumRepo;
    }

    public List<SongDTO> getAll() {
        return repo.findAll().stream()
                .map(s -> new SongDTO(s.getId(), s.getTitle(), s.getLength(), s.getLyrics(), s.getAlbum().getId()))
                .toList();
    }

    public SongDTO getById(String id) {
        Song song = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new SongDTO(song.getId(), song.getTitle(), song.getLength(), song.getLyrics(), song.getAlbum().getId());
    }

    public SongDTO create(SongWOIDDTO dto) {
        Album album = albumRepo.findById(dto.albumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Song song = new Song();
        song.setTitle(dto.title());
        song.setLength(dto.length());
        song.setLyrics(dto.lyrics());
        song.setAlbum(album);

        Song saved = repo.save(song);

        return new SongDTO(saved.getId(), saved.getTitle(), saved.getLength(), saved.getLyrics(), album.getId());
    }

    public SongDTO update(String id, SongDTO dto) {
        Song song = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Album album = albumRepo.findById(dto.album().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        song.setTitle(dto.title());
        song.setLength(dto.length());
        song.setLyrics(dto.lyrics());
        song.setAlbum(album);
        if (!album.getAlbumSongs().contains(song)) {
            album.getAlbumSongs().add(song);
        }

        Song saved = repo.save(song);

        return SongToDTO(saved);
    }

    public SongDTO patch(String id, SongDTO dto) {

        Song song = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.title() != null) song.setTitle(dto.title());
        if (dto.length() != null) song.setLength(dto.length());

        if (dto.lyrics() != null) {
            song.setLyrics(dto.lyrics());
            song.setEmbedding(embedder.getEmbedding(dto.lyrics()));
        }

        if (dto.album() != null) {
            Album newAlbum = DTOToAlbum(dto.album());
            song.setAlbum(newAlbum);
            if (!newAlbum.getAlbumSongs().contains(song)) {
                newAlbum.getAlbumSongs().add(song);
            }
        }

        if (dto.artists() != null && !dto.artists().isEmpty()) {
            song.getArtists().clear();
            dto.artists().stream()
                    .map(this::DTOToArtist)
                    .forEach(a -> {
                        song.getArtists().add(a);
                        a.getSongs().add(song);
                    });
        }

        Song saved = repo.save(song);
        return SongToDTO(saved);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}

