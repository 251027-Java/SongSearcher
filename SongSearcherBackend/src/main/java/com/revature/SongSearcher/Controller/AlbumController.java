package com.revature.SongSearcher.Controller;

import Service.AlbumService;

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

    @PutMapping("/{id}")
    public AlbumDTO update(@PathVariable String id, @RequestBody AlbumDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

