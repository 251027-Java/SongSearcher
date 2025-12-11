package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppUserDTO(
        @NotNull(message = "User ID is required")
        Long id,
        @NotBlank(message = "Username is required")
        String username) {
}
