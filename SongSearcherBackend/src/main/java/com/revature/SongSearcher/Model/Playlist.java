package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
public class Playlist {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String playlistId;

    private String playlistName;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlistId"),
            inverseJoinColumns = @JoinColumn(name = "songId")

    )
    @ToString.Exclude
    private Set<Song> songs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "userId")
    @ToString.Exclude
    private AppUser user;

    public Playlist ( String name, AppUser user) {
        this.playlistName = name;
        this.user = user;
    }

}
