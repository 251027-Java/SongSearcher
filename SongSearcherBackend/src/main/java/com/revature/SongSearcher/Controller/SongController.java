package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.SearchDTO;
import com.revature.SongSearcher.Controller.DTO.SongDTO;
import com.revature.SongSearcher.Controller.DTO.SongWOIDDTO;
import com.revature.SongSearcher.Service.AuthorizeService;
import com.revature.SongSearcher.Service.SongService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService service;
    private final AuthorizeService authService;

    public SongController(SongService service, AuthorizeService authService) {
        this.service = service;
        this.authService = authService;
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

    @GetMapping("/search/similar/{id}")
    public List<SongDTO> similarSearchById(@PathVariable String id) {
        return service.searchSimilarById(id);
    }

    @GetMapping("/recommend")
    public List<SongDTO> getUserSongRecommendations(HttpServletRequest request) {

        Long userid = authService.getUserIdFromAuthorization(request);

        return service.getUserSongRecommendations(userid);
    }
}

