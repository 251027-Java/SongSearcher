package com.revature.SongSearcher.Controller;
import com.revature.SongSearcher.Controller.DTO.ArtistDTO;
import com.revature.SongSearcher.Controller.DTO.ArtistWOIDDTO;
import com.revature.SongSearcher.Service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService service;

    public ArtistController(ArtistService service) {
        this.service = service;
    }

    @GetMapping
    public List<ArtistDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ArtistDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public ArtistDTO create(@Valid @RequestBody ArtistWOIDDTO dto) {
        return service.create(dto);
    }

//    @PutMapping("/{id}")
//    public ArtistDTO update(@PathVariable String id, @Valid @RequestBody ArtistDTO dto) {
//        return service.update(id, dto);
//    }

    @PatchMapping("/{id}")
    public ArtistDTO patch(@PathVariable String id, @Valid @RequestBody ArtistDTO dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

