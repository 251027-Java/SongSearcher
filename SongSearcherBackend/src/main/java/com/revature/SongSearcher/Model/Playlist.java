package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlists")
@Data
public class Playlist {

    @Id @GeneratedValue
    private String playlistId;

    private String playlistName;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlistId"),
            inverseJoinColumns = @JoinColumn(name = "songId")

    )
    private Set<Song> songs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private User user;

    public Playlist ( String name ) {
        this.playlistName = name;
    }

}
