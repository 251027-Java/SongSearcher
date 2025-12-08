package com.revature.SongSearcher.Controller.DTO;

import java.util.Set;

public record PlaylistDTO(String id, String name, Long userid, Set<SongDTO> songs) {
}
