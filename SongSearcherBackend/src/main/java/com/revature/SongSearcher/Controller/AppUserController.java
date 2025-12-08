package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Controller.DTO.AppUserDTO;
import com.revature.SongSearcher.Controller.DTO.AppUserWOIDDTO;
import com.revature.SongSearcher.Service.AppUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService service;

    public AppUserController (AppUserService service) {
        this.service = service;
    }

    @PostMapping()
    public AppUserDTO create(@RequestBody AppUserWOIDDTO dto) {
        return service.createUser(dto);
    }
}
