package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.PlaylistDTO;
import com.revature.SongSearcher.Controller.DTO.PlaylistWOIDDTO;
import com.revature.SongSearcher.Controller.DTO.SongID;
import com.revature.SongSearcher.Service.AuthorizeService;
import com.revature.SongSearcher.Service.PlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    //TODO
    // This mapping is unsafe, it allows all users to get all playlists.
    // Should remove for prod
    @GetMapping
    public List<PlaylistDTO> getAll() {
        return service.getAll();
    }


    @GetMapping("/user") //This is user id
    public List<PlaylistDTO> getByUserId(HttpServletRequest request) {
        Long userid = authService.getUserIdFromAuthorization(request);

        return service.getByUserId(userid);
    }

    @GetMapping("/{id}")
    public PlaylistDTO getById(@PathVariable String id, HttpServletRequest request) {

        PlaylistDTO dto = service.getById(id);

        authService.authorizeSelfAccess(request, dto.userid());

        return dto;
    }

    @PostMapping
    public PlaylistDTO create(@Valid @RequestBody PlaylistWOIDDTO dto,
                              HttpServletRequest request) {
        Long userid = authService.getUserIdFromAuthorization(request);

        return service.create(userid, dto);
    }

    @PutMapping("/{id}")
    public PlaylistDTO update(@PathVariable String id, @Valid @RequestBody PlaylistDTO dto) {
        return service.update(id, dto);
    }

    @PostMapping("/addSong/{playlistId}")
    public PlaylistDTO addSongToPlaylist(@PathVariable String playlistId,
                                         @Valid @RequestBody SongID songId,
                                         HttpServletRequest request) {

        PlaylistDTO dto = service.getById(playlistId);

        authService.authorizeSelfAccess(request, dto.userid());

        return service.addSongToPlaylist(playlistId, songId.song_id());

    }

    @PostMapping("/removeSong/{playlistId}")
    public PlaylistDTO removeSongFromPlaylist(@PathVariable String playlistId,
                                              @Valid @RequestBody SongID songId,
                                              HttpServletRequest request) {

        PlaylistDTO dto = service.getById(playlistId);

        authService.authorizeSelfAccess(request, dto.userid());

        return service.removeSongFromPlaylist(playlistId, songId.song_id());

    }

    @PatchMapping("/{id}")
    public PlaylistDTO patch(@PathVariable String id, @Valid @RequestBody PlaylistDTO dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, HttpServletRequest request) {

        PlaylistDTO dto = service.getById(id);

        authService.authorizeSelfAccess(request, dto.userid());

        service.delete(id);
    }
}


