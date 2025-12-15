package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String artistId;

    @Column(name = "artist_name")
    private String name;

    @ManyToMany(mappedBy = "artists")
    @ToString.Exclude
    private Set<Album> albums = new HashSet<>();

    @ManyToMany(mappedBy = "artists")
    @ToString.Exclude
    private Set<Song> songs = new HashSet<>();

    public Artist(String name) {
        this.name = name;
    }

    public Artist ( String id, String name) {
        this.artistId = id;
        this.name = name;
    }

}
