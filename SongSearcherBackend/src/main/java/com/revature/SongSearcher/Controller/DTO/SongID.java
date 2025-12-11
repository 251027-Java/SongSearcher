package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;

public record SongID(
        @NotBlank(message = "Song ID is required")
        String song_id) {
}
