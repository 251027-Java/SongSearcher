package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;

public record SearchDTO (
        @NotBlank(message = "Lyrics are required")
        String lyrics) {
}
