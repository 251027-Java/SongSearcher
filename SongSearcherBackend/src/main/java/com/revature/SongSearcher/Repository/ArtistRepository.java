package com.revature.SongSearcher.Repository;

import com.revature.SongSearcher.Model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {}

