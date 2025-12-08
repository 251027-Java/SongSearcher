package com.revature.SongSearcher.Controller.DTO;

import java.util.Set;

public record PlaylistWOIDDTO(String name, Long userid, Set<SongDTO> songs) {
}
