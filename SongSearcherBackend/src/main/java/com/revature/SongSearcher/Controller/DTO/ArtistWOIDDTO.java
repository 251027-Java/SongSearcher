package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;

public record ArtistWOIDDTO(
        @NotBlank(message = "Artist Name is required")
        String name) {}
