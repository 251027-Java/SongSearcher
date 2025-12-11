package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AlbumWOIDDTO(
        @NotBlank(message = "Album Title is required")
        String title,
        @NotNull(message = "Album Release Year is required")
        int releaseYear,
        @NotNull(message = "Artist is required")
        List<ArtistDTO> artists) {}
