package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.PlaylistDTO;
import com.revature.SongSearcher.Controller.DTO.PlaylistWOIDDTO;
import com.revature.SongSearcher.Service.AuthorizeService;
import com.revature.SongSearcher.Service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService service;
    private final AuthorizeService authService;

    public PlaylistController(PlaylistService service,
                              AuthorizeService authService) {
        this.service = service;
        this.authService = authService;
    }

//    @GetMapping
//    public List<PlaylistDTO> getAll() {
//        return service.getAll();
//    }


    @GetMapping("/user/{userid}") //This is user id
    public List<PlaylistDTO> getByUserId(@PathVariable Long userid, HttpServletRequest request) {
        authService.authorizeSelfAccess(request, userid);

        return service.getByUserId(userid);
    }

    @GetMapping("/{id}")
    public PlaylistDTO getById(@PathVariable String id, HttpServletRequest request) {

        PlaylistDTO dto = service.getById(id);

        authService.authorizeSelfAccess(request, dto.userid());

        return dto;
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
    public void delete(@PathVariable String id, HttpServletRequest request) {

        PlaylistDTO dto = service.getById(id);

        authService.authorizeSelfAccess(request, dto.userid());

        service.delete(id);
    }
}


