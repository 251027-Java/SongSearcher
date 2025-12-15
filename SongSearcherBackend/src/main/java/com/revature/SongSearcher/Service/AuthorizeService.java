package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Model.AppUser;
import com.revature.SongSearcher.Repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class AuthorizeService {

    private final AppUserRepository userRepo;

    public AuthorizeService ( AppUserRepository repo ) {
        this.userRepo = repo;
    }

    private boolean isAdmin ( Long id ) {
        Optional<AppUser> user = userRepo.findById(id);

        return user.map(appUser -> appUser.getUser_role().equals("ADMIN")).orElse(false);
    }

    public void authorizeSelfAccess(HttpServletRequest request, Long pathUserId) {
        Long authId = (Long) request.getAttribute("authenticatedUserId");

        if (!authId.equals(pathUserId) && !isAdmin(authId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }
    }

    public Long getUserIdFromAuthorization(HttpServletRequest request) {
        return (Long) request.getAttribute("authenticatedUserId");
    }

}
