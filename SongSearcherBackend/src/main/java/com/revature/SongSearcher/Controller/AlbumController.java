package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.AlbumDTO;
import com.revature.SongSearcher.Controller.DTO.AlbumWOIDDTO;
import com.revature.SongSearcher.Service.AlbumService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService service;

    public AlbumController(AlbumService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlbumDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AlbumDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public AlbumDTO create(@RequestBody AlbumWOIDDTO dto) {
        return service.create(dto);
    }

//    @PutMapping("/{id}")
//    public AlbumDTO update(@PathVariable String id, @RequestBody AlbumDTO dto) {
//        return service.update(id, dto);
//    }

    @PatchMapping("/{id}")
    public AlbumDTO patch(@PathVariable String id, @RequestBody AlbumDTO dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

