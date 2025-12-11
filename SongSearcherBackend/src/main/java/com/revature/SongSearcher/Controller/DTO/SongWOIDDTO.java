package com.revature.SongSearcher.Controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record SongWOIDDTO(
              @NotBlank(message = "Song Title is required")
              String title,
              @NotNull(message = "Song Length is required")
              BigDecimal length,
              @NotBlank(message = "Song Lyrics are required")
              String lyrics,
              @NotNull(message = "Song Album is required")
              AlbumSlimDTO album,
              List<ArtistDTO> artists) {}

