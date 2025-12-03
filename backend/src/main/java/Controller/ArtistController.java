package Controller;

import Service.ArtistService;

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
    public ArtistDTO create(@RequestBody ArtistWOIDDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ArtistDTO update(@PathVariable String id, @RequestBody ArtistDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}

