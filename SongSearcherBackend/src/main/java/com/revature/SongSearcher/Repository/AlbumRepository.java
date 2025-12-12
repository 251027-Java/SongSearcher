package com.revature.SongSearcher.Repository;

import com.revature.SongSearcher.Model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, String> {

    public Album findByTitleAndReleaseyear(String title, int release_year);
}

