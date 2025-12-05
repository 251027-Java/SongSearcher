package com.revature.SongSearcher.Controller;

import java.math.BigDecimal;
import java.util.List;

public record SongWOIDDTO(String title, BigDecimal length, String lyrics, AlbumDTO album, List<ArtistDTO> artists) {}

