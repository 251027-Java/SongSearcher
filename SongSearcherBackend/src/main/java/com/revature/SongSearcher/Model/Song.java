package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import com.vladmihalcea.hibernate.type.array.DoubleArrayType;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "songs")
@Data
public class Song {

    @Id @GeneratedValue
    private String songId;

    @Column(name = "song_title")
    private String title;

    private BigDecimal length;

    @Column(name = "song_lyrics", columnDefinition = "TEXT")
    private String lyrics;


    @Type(DoubleArrayType.class)
    @Column(name = "embedding", columnDefinition = "vector(100)")
    @ToString.Exclude
    private Double[] embedding;

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

    public Song(String title, BigDecimal length, String lyrics) {
        this.title = title;
        this.length = length;
        this.lyrics = lyrics;
    }

    public Song(String title, BigDecimal length, String lyrics, Double[] embedding) {
        this.title = title;
        this.length = length;
        this.lyrics = lyrics;
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        return String.format("Song: \"%s\", Artist: %s, Album: %s (%d), Duration: %.2f",
                this.title, this.album.getArtists().toString(), this.album, this.album.getRelease_year(), this.length);
    }
}
