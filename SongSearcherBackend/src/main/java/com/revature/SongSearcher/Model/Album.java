package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "albums")
@Data
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue
    private String albumId;

    @Column(name = "album_title")
    private String title;

    private int release_year;

    @ManyToMany(mappedBy = "albums")
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @ToString.Exclude
    private List<Song> albumSongs = new ArrayList<>();

    public Album(String title, int release_year, Set<Artist> artists) {
        this.title = title;
        this.release_year = release_year;
        this.artists = artists;
    }

    public String toString() {
        return "Album: " + this.title + " (" + this.release_year + ")";
    }
}
