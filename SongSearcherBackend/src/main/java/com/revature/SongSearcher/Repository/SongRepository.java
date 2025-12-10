package com.revature.SongSearcher.Repository;

import com.revature.SongSearcher.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, String> {

    List<Song> findByTitleContainingIgnoreCase(String title);
    List<Song> findByAlbum_TitleContainingIgnoreCase(String title);

    //Probably need to write a custom query for this
    @Query("""
        SELECT DISTINCT s FROM Song s
        LEFT JOIN s.album a
        LEFT JOIN a.artists primaryArtist
        LEFT JOIN s.artists additionalArtist
        WHERE LOWER(primaryArtist.name) LIKE LOWER(CONCAT('%', :artistName, '%'))
           OR LOWER(additionalArtist.name) LIKE LOWER(CONCAT('%', :artistName, '%'))
    """)
    List<Song> findSongsByArtistName(@Param("artistName") String artistName);

    @Query(
            value = """
            SELECT *
            FROM songs
            WHERE song_id <> :songId
            ORDER BY embedding <-> CAST(:embedding AS vector)
            LIMIT :limit
            """,
            nativeQuery = true
    )
    List<Song> findMostSimilarTo(
            @Param("songId") String songId,
            @Param("embedding") float[] embedding,
            @Param("limit") int limit
    );

    @Query(
            value = """
            SELECT *
            FROM songs
            ORDER BY embedding <-> CAST(:embedding AS vector)
            LIMIT :limit
            """,
            nativeQuery = true
    )
    List<Song> findMostSimilar(
            @Param("embedding") float[] embedding,
            @Param("limit") int limit
    );

    @Query(
            value = """
        SELECT * FROM songs
        WHERE song_id NOT IN (:exclude)
        ORDER BY embedding <-> CAST(:target AS vector)
        LIMIT :limit
        """,
            nativeQuery = true
    )
    List<Song> recommend(
            @Param("target") float[] targetVector,
            @Param("exclude") List<String> exclude,
            @Param("limit") int limit
    );

}

