package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.*;
import com.revature.SongSearcher.Model.AppUser;
import com.revature.SongSearcher.Repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;
    private final PlaylistService playlistService;

    public AppUserService(AppUserRepository repo,
                          PasswordEncoder encoder,
                          PlaylistService playlistService) {
        this.repo = repo;
        this.encoder = encoder;
        this.playlistService = playlistService;
    }

    private AppUserDTO AppUserToDTO(AppUser user) {
        return new AppUserDTO(user.getUserId(), user.getUsername());
    }

    // TODO
    // Create 'favorites' playlist when user is created.
    public AppUserDTO createUser(AppUserWOIDDTO dto) {

        Optional<AppUser> existing = repo.findByUsername(dto.username());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.IM_USED);
        }

        AppUser user = new AppUser(dto.username(), encoder.encode(dto.password()), "USER");

        PlaylistWOIDDTO favoritesDTO = new PlaylistWOIDDTO("Favorites", user.getUserId(), Set.of());
        PlaylistDTO favorites = playlistService.create(favoritesDTO);

        return AppUserToDTO(repo.save(user));
    }

    public AppUserDTO createAdmin(AppUserWOIDDTO dto) {

        Optional<AppUser> existing = repo.findByUsername(dto.username());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.IM_USED);
        }

        AppUser admin = new AppUser(dto.username(), encoder.encode(dto.password()), "ADMIN");

        return AppUserToDTO(repo.save(admin));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

