package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
public class Song {

    @Id @GeneratedValue
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
    private Album album;

    @ManyToMany
    @JoinTable(
            name = "artist_song",
            joinColumns = @JoinColumn(name = "songId"),
            inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany(mappedBy = "songs")
    @ToString.Exclude
    private Set<Playlist> playlists = new HashSet<>();

//    public Song(String title, BigDecimal length, String lyrics) {
//        this.title = title;
//        this.length = length;
//        this.lyrics = lyrics;
//    }

    public Song(String title, BigDecimal length, String lyrics, Set<Artist> artists, float[] embedding) {
        this.title = title;
        this.length = length;
        this.lyrics = lyrics;
        this.artists = artists;
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return String.format("Song: \"%s\", Artist: %s, Album: %s (%d), Duration: %.2f",
                this.title, this.album.getArtists().toString(), this.album, this.album.getRelease_year(), this.length);
    }
}
