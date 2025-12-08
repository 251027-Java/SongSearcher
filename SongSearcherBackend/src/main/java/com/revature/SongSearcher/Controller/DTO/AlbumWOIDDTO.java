package com.revature.SongSearcher.Controller.DTO;

import java.util.List;

public record AlbumWOIDDTO(String title, int releaseYear, List<ArtistDTO> artists) {}
