package com.revature.SongSearcher.Repository;
import com.revature.SongSearcher.Controller.PlaylistDTO;
import com.revature.SongSearcher.Model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    @Query(
            value = """
            SELECT *
            FROM playlists
            WHERE user_id = :userId
            """,
            nativeQuery = true
    )
    public List<Playlist> findByUser_UserId (
            @Param("userId") Long userId);
}
