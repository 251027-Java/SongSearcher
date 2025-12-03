package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.ArtistDTO;
import com.revature.SongSearcher.Controller.ArtistWOIDDTO;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Repository.ArtistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository repo;

    public ArtistService(ArtistRepository repo) {
        this.repo = repo;
    }

    public List<ArtistDTO> getAll() {
        return repo.findAll().stream()
                .map(a -> new ArtistDTO(a.getId(), a.getName()))
                .toList();
    }

    public ArtistDTO getById(String id) {
        Artist a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new ArtistDTO(a.getId(), a.getName());
    }

    public ArtistDTO create(ArtistWOIDDTO dto) {
        Artist a = new Artist();
        a.setName(dto.name());
        return new ArtistDTO(repo.save(a).getId(), a.getName());
    }

    public ArtistDTO update(String id, ArtistDTO dto) {
        Artist a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        a.setName(dto.name());
        return new ArtistDTO(repo.save(a).getId(), a.getName());
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
