package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.SearchDTO;
import com.revature.SongSearcher.Controller.DTO.SongDTO;
import com.revature.SongSearcher.Controller.DTO.SongWOIDDTO;
import com.revature.SongSearcher.Service.SongService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService service;

    public SongController(SongService service) {
        this.service = service;
    }

    @GetMapping
    public List<SongDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SongDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public SongDTO create(@Valid @RequestBody SongWOIDDTO dto) {
        return service.create(dto);
    }

//    @PutMapping("/{id}")
//    public SongDTO update(@PathVariable String id, @Valid @RequestBody SongDTO dto) {
//        return service.update(id, dto);
//    }

    @PatchMapping("/{id}")
    public SongDTO patch(@PathVariable String id, @Valid @RequestBody SongDTO dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/search/title/{title}")
    public List<SongDTO> searchTitle(@PathVariable String title) {
        return service.searchByTitle(title);
    }

    @GetMapping("/search/album/{title}")
    public List<SongDTO> searchAlbum(@PathVariable String title) {
        return service.searchByAlbum(title);
    }

    @GetMapping("/search/artist/{name}")
    public List<SongDTO> searchArtist(@PathVariable String name) {
        return service.searchByArtist(name);
    }

    @PostMapping("/search/similar")
    public List<SongDTO> searchByLyrics(@Valid @RequestBody SearchDTO dto) {
        return service.searchByLyrics(dto);
    }

    @GetMapping("/recommend/{userid}")
    public List<SongDTO> getUserSongRecommendations(@PathVariable Long userid) {
        return service.getUserSongRecommendations(userid);
    }
}

