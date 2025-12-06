package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Model.AppUser;

import java.util.Set;

public record PlaylistDTO(String id, String name, Long userid, Set<SongDTO> songs) {
}
