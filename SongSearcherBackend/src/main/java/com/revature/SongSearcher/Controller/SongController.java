package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Service.SongService;
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
    public SongDTO create(@RequestBody SongWOIDDTO dto) {
        return service.create(dto);
    }

//    @PutMapping("/{id}")
//    public SongDTO update(@PathVariable String id, @RequestBody SongDTO dto) {
//        return service.update(id, dto);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/similar")
    public List<SongDTO> searchByLyrics(@RequestBody SearchDTO dto) {
        return service.searchByLyrics(dto);
    }
}

