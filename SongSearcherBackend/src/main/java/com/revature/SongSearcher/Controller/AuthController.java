package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.AppUserDTO;
import com.revature.SongSearcher.Controller.DTO.AppUserWOIDDTO;
import com.revature.SongSearcher.Utils.JwtUtil;
import com.revature.SongSearcher.Model.AppUser;
import com.revature.SongSearcher.Repository.AppUserRepository;
import com.revature.SongSearcher.Service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// Use your own records
//record AuthRequest(String username, String password) {}
//record AuthResponse(String token) {}

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Auth Records
    public record AuthRequest(String username, String password){}
    public record AuthResponse(String token){}

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AppUserService service;

    public AuthController(AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          AppUserService service) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request){

        Optional<AppUser> user = appUserRepository.findByUsername(request.username());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (!passwordEncoder.matches(request.password(), user.get().getUserpassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = jwtUtil.generateToken(String.valueOf(user.get().getUserId()));

        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody AppUserWOIDDTO dto) {

        AppUserDTO user =  service.createUser(dto);

        String token = jwtUtil.generateToken(String.valueOf(user.id()));

        return new AuthResponse(token);
    }
}
