package com.revature.SongSearcher.Repository;
import com.revature.SongSearcher.Model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, String> {
}
