package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "albums")
@Getter
@Setter
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String albumId;

    @Column(name = "album_title")
    private String title;

    private int releaseyear;

    @ManyToMany
    @JoinTable(
            name = "artist_album",
            joinColumns = @JoinColumn(name = "albumId"),
            inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    @ToString.Exclude
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @ToString.Exclude
    private List<Song> albumSongs = new ArrayList<>();

    public Album(String title, int releaseyear, Set<Artist> artists) {
        this.title = title;
        this.releaseyear = releaseyear;
        this.artists = artists;
    }

    public Album(String id, String title, int releaseyear, Set<Artist> artists) {
        this.albumId = id;
        this.title = title;
        this.releaseyear = releaseyear;
        this.artists = artists;
    }

}
