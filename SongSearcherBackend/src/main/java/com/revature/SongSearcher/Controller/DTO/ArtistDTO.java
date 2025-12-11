package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;

public record ArtistDTO(
        @NotBlank(message = "Artist ID is required")
        String id,
        @NotBlank(message = "Artist Name is required")
        String name) {}
