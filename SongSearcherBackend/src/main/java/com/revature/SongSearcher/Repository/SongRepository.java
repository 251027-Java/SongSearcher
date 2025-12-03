package com.revature.SongSearcher.Repository;

import com.revature.SongSearcher.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {}

