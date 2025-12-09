package com.revature.SongSearcher.Repository;

import com.revature.SongSearcher.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, String> {

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

