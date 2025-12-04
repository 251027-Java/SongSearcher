package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "albums")
@Data
public class Album {

    @Id
    @GeneratedValue
    private String albumId;

    @Column(name = "album_title")
    private final String title;

    private final int release_year;

    @ManyToMany(mappedBy = "albums")
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @ToString.Exclude
    private List<Song> albumSongs = new ArrayList<>();

    public Album(String title, int release_year) {
        this.title = title;
        this.release_year = release_year;
    }

    public String toString() {
        return "Album: " + this.title + " (" + this.release_year + ")";
    }
}
