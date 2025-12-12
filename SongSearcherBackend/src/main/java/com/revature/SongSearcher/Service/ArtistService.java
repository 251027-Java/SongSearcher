package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.ArtistDTO;
import com.revature.SongSearcher.Controller.DTO.ArtistWOIDDTO;
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

    private ArtistDTO ArtistToDTO(Artist artist ) {
        return new ArtistDTO(artist.getArtistId(), artist.getName());
    }
    private Artist DTOToArtist ( ArtistDTO dto ) {
        return new Artist(dto.id(), dto.name());
    }

    public List<ArtistDTO> getAll() {
        return repo.findAll().stream()
                .map(this::ArtistToDTO)
                .toList();
    }

    public ArtistDTO getById(String id) {
        Artist a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ArtistToDTO(a);
    }

    public ArtistDTO create(ArtistWOIDDTO dto) {
        Artist a = new Artist(dto.name());
        return ArtistToDTO(repo.save(a));
    }

    public ArtistDTO update(String id, ArtistDTO dto) {
        Artist a = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        a.setName(dto.name());
        Artist saved = repo.save(a);

        return ArtistToDTO(saved);
//        return new ArtistDTO(repo.save(a).getId(), a.getName());
    }

    public ArtistDTO patch(String id, ArtistDTO dto) {

        Artist artist = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.name() != null)
            artist.setName(dto.name());

        Artist saved = repo.save(artist);

        return ArtistToDTO(saved);
    }


    public void delete(String id) {
        repo.deleteById(id);
    }

    public ArtistDTO getByName (String name) {
        Artist artist = this.repo.findByName(name);
        return artist!=null ? ArtistToDTO(this.repo.findByName(name)) : null;
    }

    public void flush() {
        repo.flush();
    }
}
