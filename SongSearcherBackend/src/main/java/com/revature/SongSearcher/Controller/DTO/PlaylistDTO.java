package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PlaylistDTO(
        @NotBlank(message = "Playlist ID is required")
        String id,
        @NotBlank(message = "Playlist Name is required")
        String name,
        @NotNull
        Long userid,
        Set<SongDTO> songs) {
}
