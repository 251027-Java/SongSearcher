package com.revature.SongSearcher;

import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Model.Song;

public interface IRepository {
    public boolean createArtist(Artist artist);
    //Other CRUD for com.revature.SongSearcher.Model.Artist
    public Artist getArtist(String name);
    public boolean deleteArtist(String name);

    public boolean createAlbum(Album album);
    //Other CRUD for com.revature.SongSearcher.Model.Album
    public Album getAlbum(String name, int release_year);
    public boolean deleteAlbum(String name, int release_year);

    public boolean createSong(Song song); //repo manages internal ids, abstract from caller
    //Other CRUD for com.revature.SongSearcher.Model.Song
    public Song getSong(String title, String album_name, int release_year);
    public boolean deleteSong(String title, String album_name, int release_year);
}
