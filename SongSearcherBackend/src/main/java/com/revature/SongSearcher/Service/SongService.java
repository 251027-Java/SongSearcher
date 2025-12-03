package com.revature.SongSearcher.Service;

import Controller.SongDTO;
import Controller.SongWOIDDTO;

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

        Album album = albumRepo.findById(dto.albumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        song.setTitle(dto.title());
        song.setLength(dto.length());
        song.setLyrics(dto.lyrics());
        song.setAlbum(album);

        repo.save(song);

        return dto;
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}

