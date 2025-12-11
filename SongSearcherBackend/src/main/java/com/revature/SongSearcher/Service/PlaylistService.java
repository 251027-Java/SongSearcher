package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.*;
import com.revature.SongSearcher.Model.*;
import com.revature.SongSearcher.Repository.AppUserRepository;
import com.revature.SongSearcher.Repository.PlaylistRepository;
import com.revature.SongSearcher.Repository.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository repo;
    private final AppUserRepository userRepo;
    private final SongRepository songRepo;

    public PlaylistService(PlaylistRepository repo,
                           AppUserRepository userRepo,
                           SongRepository songRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.songRepo = songRepo;
    }

    private ArtistDTO ArtistToDTO(Artist artist ) {
        return new ArtistDTO(artist.getArtistId(), artist.getName());
    }
    private AlbumDTO AlbumToDTO (Album album) {
        return new AlbumDTO(album.getAlbumId(), album.getTitle(), album.getRelease_year(),
                new ArrayList<>(album.getArtists()).stream().map(this::ArtistToDTO).toList());
    }
    private AlbumSlimDTO AlbumToSlimDTO (Album album ) {
        return new AlbumSlimDTO(
                album.getAlbumId(),
                album.getTitle(),
                album.getRelease_year()
        );
    }
    private SongDTO SongToDTO (Song song) {

        List<ArtistDTO> artists  = new ArrayList<>(new ArrayList<>(song.getAlbum().getArtists()).stream().map(this::ArtistToDTO).toList());
        List<ArtistDTO> secondaries = new ArrayList<>(song.getArtists()).stream().map(this::ArtistToDTO).toList();
        artists.addAll(secondaries);

        return new SongDTO(song.getSongId(), song.getTitle(), song.getLength(),
                song.getLyrics(),
                AlbumToSlimDTO(song.getAlbum()),
                artists);
    }

    private PlaylistDTO PlaylistToDTO (Playlist playlist) {
        return new PlaylistDTO(playlist.getPlaylistId(),
                playlist.getPlaylistName(),
                playlist.getUser().getUserId(),
                new ArrayList<>(playlist.getSongs()).stream().map(this::SongToDTO).collect(Collectors.toSet()));
    }
    private Playlist DTOToPlaylist (Long userid, PlaylistWOIDDTO dto) {
        AppUser user = userRepo.findById(userid).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return new Playlist(dto.name(), user);
    }

    public List<PlaylistDTO> getAll() {
        return repo.findAll().stream()
                .map(this::PlaylistToDTO)
                .toList();
    }

    public PlaylistDTO getById(String id) {
        Playlist playlist = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return PlaylistToDTO(playlist);
    }

    public List<PlaylistDTO> getByUserId(Long userId) {
        return repo.findByUser_UserId(userId).stream().map(this::PlaylistToDTO).toList();
    }

    public PlaylistDTO create(Long userid, PlaylistWOIDDTO dto) {

        Playlist playlist = DTOToPlaylist(userid, dto);

        List<Optional<Song>> songs = dto.songs().stream().map(s -> songRepo.findById(s.id())).toList();

        for (Optional<Song> song : songs) {
            song.ifPresent(value -> playlist.getSongs().add(value));
        }

        return PlaylistToDTO(this.repo.save(playlist));

    }

    // update
    public PlaylistDTO update(String id, PlaylistDTO dto) {

        Playlist playlist = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        playlist.setPlaylistName(dto.name());

        if (dto.userid() != null) {
            AppUser user = userRepo.findById(dto.userid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            playlist.setUser(user);
        }

        playlist.getSongs().clear();

        dto.songs().stream()
                .map(songDto -> songRepo.findById(songDto.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .forEach(song -> playlist.getSongs().add(song));

        Playlist saved = repo.save(playlist);

        return PlaylistToDTO(saved);
    }

    public PlaylistDTO patch(String id, PlaylistDTO dto) {

        Playlist playlist = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.name() != null) {
            playlist.setPlaylistName(dto.name());
        }

        if (dto.userid() != null) {
            AppUser user = userRepo.findById(dto.userid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            playlist.setUser(user);
        }

        if (dto.songs() != null) {
            playlist.getSongs().clear();

            dto.songs().stream()
                    .map(songDto -> songRepo.findById(songDto.id())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .forEach(song -> playlist.getSongs().add(song));
        }

        Playlist saved = repo.save(playlist);

        return PlaylistToDTO(saved);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public PlaylistDTO getByUserIdAndName(Long userId, String playlistName) {
        return PlaylistToDTO(repo.findByPlaylistNameAndUser_UserId(playlistName, userId));
    }

    public PlaylistDTO addSongToPlaylist(String playlistId, String songId) {
        Playlist playlist = repo.findById(playlistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"));

        Song song = songRepo.findById(songId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found"));

        playlist.getSongs().add(song);

        return PlaylistToDTO(repo.save(playlist));
    }

    public PlaylistDTO removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = repo.findById(playlistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"));

        Song song = songRepo.findById(songId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found"));

        playlist.getSongs().remove(song);

        return PlaylistToDTO(repo.save(playlist));
    }
}

