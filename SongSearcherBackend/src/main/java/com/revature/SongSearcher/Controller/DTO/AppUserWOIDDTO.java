package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppUserWOIDDTO(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password) {
}
