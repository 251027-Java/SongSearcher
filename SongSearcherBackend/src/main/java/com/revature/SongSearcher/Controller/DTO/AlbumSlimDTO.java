package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlbumSlimDTO(
        @NotBlank(message = "Album ID is required")
        String id,
        @NotBlank(message = "Album Title is required")
        String title,
        @NotNull(message = "Album Release Year is required")
        int releaseYear
) {
}
