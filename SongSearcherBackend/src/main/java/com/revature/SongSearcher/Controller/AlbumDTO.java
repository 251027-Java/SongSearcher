package com.revature.SongSearcher.Controller;

import java.util.List;

public record AlbumDTO(String id, String title, int releaseYear, List<ArtistDTO> artists) {}
