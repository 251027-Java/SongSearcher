package com.revature.SongSearcher.Controller.DTO;

import java.math.BigDecimal;
import java.util.List;

public record SongDTO(String id, String title, BigDecimal length, String lyrics, AlbumSlimDTO album, List<ArtistDTO> artists) {}
