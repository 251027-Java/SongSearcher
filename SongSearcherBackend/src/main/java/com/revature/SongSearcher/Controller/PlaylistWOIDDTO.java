package com.revature.SongSearcher.Controller;

import com.revature.SongSearcher.Model.AppUser;

import java.util.Set;

public record PlaylistWOIDDTO(String id, String name, Long userid, Set<SongDTO> songs) {
}
