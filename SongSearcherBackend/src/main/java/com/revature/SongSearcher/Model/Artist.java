package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Data
public class Artist {

    @Id
    @GeneratedValue
    private String artistId;

    @Column(name = "artist_name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "artist_album",
            joinColumns = @JoinColumn(name = "artistId"),
            inverseJoinColumns = @JoinColumn(name = "albumId")
    )
    @ToString.Exclude
    private Set<Album> albums = new HashSet<>();

    @ManyToMany(mappedBy = "artists")
    @ToString.Exclude
    private Set<Song> songs = new HashSet<>();

    public Artist(String name) {
        this.name = name;
    }

    public String toString() {
        return "Artist: " + this.getName();
    }
}
