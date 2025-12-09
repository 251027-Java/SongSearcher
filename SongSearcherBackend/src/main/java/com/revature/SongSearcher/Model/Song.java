package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
public class Song {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String songId;

    @Column(name = "song_title")
    private String title;

    private BigDecimal length;

    @Column(name = "song_lyrics", columnDefinition = "TEXT")
    private String lyrics;

    @Column
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 100)
    @ToString.Exclude
    private float[] embedding;

    @ManyToOne()
    @JoinColumn(name = "albumId")
    @ToString.Exclude
    private Album album;

    @ManyToMany
    @JoinTable(
            name = "artist_song",
            joinColumns = @JoinColumn(name = "songId"),
            inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    @ToString.Exclude
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany(mappedBy = "songs")
    @ToString.Exclude
    private Set<Playlist> playlists = new HashSet<>();

    public Song(String title, BigDecimal length, Album album, Set<Artist> additionalArtists, String lyrics, float[] embedding) {
        this.title = title;
        this.length = length;
        this.album = album;
        this.lyrics = lyrics;
        this.artists = additionalArtists;
        this.embedding = embedding;
    }

    public Song(String id, String title, BigDecimal length, Album album, Set<Artist> additionalArtists, String lyrics, float[] embedding) {
        this.songId = id;
        this.title = title;
        this.length = length;
        this.album = album;
        this.lyrics = lyrics;
        this.artists = additionalArtists;
        this.embedding = embedding;
    }

//    @Override
//    public String toString() {
//        return String.format("Song: \"%s\", Artist: %s, Album: %s (%d), Duration: %.2f",
//                this.title, this.album.getArtists().toString(), this.album, this.album.getRelease_year(), this.length);
//    }
}
