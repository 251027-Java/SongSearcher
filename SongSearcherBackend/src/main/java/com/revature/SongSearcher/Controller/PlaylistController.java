package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.PlaylistDTO;
import com.revature.SongSearcher.Controller.DTO.PlaylistWOIDDTO;
import com.revature.SongSearcher.Service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlaylistDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/user/{userid}") //This is user id
    public List<PlaylistDTO> getByUserId(@PathVariable Long userid) {
        return service.getByUserId(userid);
    }

    @GetMapping("/{id}")
    public PlaylistDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public PlaylistDTO create(@RequestBody PlaylistWOIDDTO dto) {
        return service.create(dto);
    }

//    @PutMapping("/{id}")
//    public PlaylistDTO update(@PathVariable String id, @RequestBody PlayList dto) {
//        return service.update(id, dto);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}


