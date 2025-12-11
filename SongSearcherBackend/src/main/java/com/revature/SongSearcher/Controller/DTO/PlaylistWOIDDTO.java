package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PlaylistWOIDDTO(@NotBlank(message = "Playlist Name is required")
                              String name,
                              Set<SongDTO> songs) {
}
