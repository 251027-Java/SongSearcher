package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.AlbumSlimDTO;
import com.revature.SongSearcher.Controller.DTO.PlaylistDTO;
import com.revature.SongSearcher.Controller.DTO.PlaylistWOIDDTO;
import com.revature.SongSearcher.Controller.DTO.SongDTO;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.AppUser;
import com.revature.SongSearcher.Model.Playlist;
import com.revature.SongSearcher.Model.Song;
import com.revature.SongSearcher.Repository.AppUserRepository;
import com.revature.SongSearcher.Repository.PlaylistRepository;
import com.revature.SongSearcher.Repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTests {

    @Mock
    private PlaylistRepository playlistRepo;

    @Mock
    private AppUserRepository userRepo;

    @Mock
    private SongRepository songRepo;

    @InjectMocks
    private PlaylistService service;

    /* ==========================================================
         GET ALL
       ========================================================== */
    @Test
    public void happyPath_getAll_returnsListOfPlaylistDTOs() {
        // Arrange
        AppUser user = new AppUser("john", "pass", "USER");
        user.setUserId(1L);

        Playlist p1 = new Playlist("Chill", user);
        p1.setPlaylistId("p1");

        Playlist p2 = new Playlist("Rock", user);
        p2.setPlaylistId("p2");

        when(playlistRepo.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<PlaylistDTO> actual = service.getAll();

        // Assert
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).id()).isEqualTo("p1");
        assertThat(actual.get(1).id()).isEqualTo("p2");
    }

    /* ==========================================================
         GET BY ID
       ========================================================== */
    @Test
    public void happyPath_getById_returnsPlaylistDTO() {
        // Arrange
        AppUser user = new AppUser("john", "pass", "USER");
        user.setUserId(1L);

        Playlist p = new Playlist("Favorites", user);
        p.setPlaylistId("abc123");

        when(playlistRepo.findById("abc123")).thenReturn(Optional.of(p));

        // Act
        PlaylistDTO actual = service.getById("abc123");

        // Assert
        assertThat(actual.id()).isEqualTo("abc123");
        assertThat(actual.name()).isEqualTo("Favorites");
        assertThat(actual.userid()).isEqualTo(1L);
    }

    @Test
    public void sadPath_getById_notFound_throws404() {
        // Arrange
        when(playlistRepo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getById("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         GET BY USER ID
       ========================================================== */
    @Test
    public void happyPath_getByUserId_returnsList() {
        // Arrange
        AppUser user = new AppUser("john", "pw", "USER");
        user.setUserId(1L);

        Playlist p = new Playlist("Gym Mix", user);
        p.setPlaylistId("gym");

        when(playlistRepo.findByUser_UserId(1L)).thenReturn(List.of(p));

        // Act
        List<PlaylistDTO> actual = service.getByUserId(1L);

        // Assert
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).name()).isEqualTo("Gym Mix");
    }

    /* ==========================================================
         CREATE
       ========================================================== */
    @Test
    public void happyPath_createPlaylist_returnsDTO() {
        // Arrange
        AppUser user = new AppUser("john", "pass", "USER");
        user.setUserId(10L);

        PlaylistWOIDDTO dto = new PlaylistWOIDDTO("Study", Set.of());

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));

        Playlist saved = new Playlist("Study", user);
        saved.setPlaylistId("p123");
        when(playlistRepo.save(any(Playlist.class))).thenReturn(saved);

        // Act
        PlaylistDTO actual = service.create(user.getUserId(), dto);

        // Assert
        assertThat(actual.id()).isEqualTo("p123");
        assertThat(actual.name()).isEqualTo("Study");
        assertThat(actual.userid()).isEqualTo(10L);
    }

    @Test
    public void sadPath_createPlaylist_userNotFound_throws400() {
        // Arrange
        PlaylistWOIDDTO dto = new PlaylistWOIDDTO("Study", Set.of());
        when(userRepo.findById(77L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.create(77L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400");
    }

    @Test
    public void happyPath_createPlaylist_addsSongsIfTheyExist() {
        // Arrange
        AppUser user = new AppUser("john", "pw", "USER");
        user.setUserId(1L);

        Album album = new Album();
        album.setAlbumId("album1");
        album.setTitle("AlbumTitle");
        album.setRelease_year(2024);

        Song song = new Song();
        song.setSongId("song1");
        song.setAlbum(album);
        song.setTitle("Test Song");
        song.setLength(new BigDecimal("3.50"));
        song.setLyrics("lyrics...");
        song.setArtists(new HashSet<>());

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(songRepo.findById("song1")).thenReturn(Optional.of(song));

        // Construct SongDTO properly
        SongDTO songDto = new SongDTO(
                "song1",
                "Test Song",
                new BigDecimal("3.50"),
                "lyrics...",
                new AlbumSlimDTO("album1", "AlbumTitle", 2024),
                List.of() // no artists for this test
        );

        PlaylistWOIDDTO dto = new PlaylistWOIDDTO("Workout", Set.of(songDto));

        Playlist savedPlaylist = new Playlist("Workout", user);
        savedPlaylist.setPlaylistId("pid");
        savedPlaylist.getSongs().add(song);

        when(playlistRepo.save(any(Playlist.class))).thenReturn(savedPlaylist);

        // Act
        PlaylistDTO actual = service.create(user.getUserId(), dto);

        // Assert
        assertThat(actual.songs()).hasSize(1);
        assertThat(actual.songs().iterator().next().id()).isEqualTo("song1");
    }


    /* ==========================================================
         UPDATE
       ========================================================== */
    @Test
    public void happyPath_update_updatesFieldsCorrectly() {
        // Arrange
        AppUser oldUser = new AppUser("old", "pw1", "USER");
        oldUser.setUserId(1L);

        AppUser newUser = new AppUser("new", "pw2", "USER");
        newUser.setUserId(2L);

        Playlist playlist = new Playlist("OldName", oldUser);
        playlist.setPlaylistId("pid");

        // --- FIX: Song must have a valid Album ---
        Album album = new Album();
        album.setAlbumId("a1");
        album.setTitle("AlbumTitle");
        album.setRelease_year(2024);
        album.setArtists(new HashSet<>()); // required to avoid NPE

        Song song = new Song();
        song.setSongId("s1");
        song.setAlbum(album);
        song.setTitle("SongTitle");
        song.setLength(new BigDecimal("3.50"));
        song.setLyrics("lyrics...");
        song.setArtists(new HashSet<>());

        // Proper SongDTO
        SongDTO songDto = new SongDTO(
                "s1",
                "SongTitle",
                new BigDecimal("3.50"),
                "lyrics...",
                new AlbumSlimDTO("a1", "AlbumTitle", 2024),
                List.of()
        );

        PlaylistDTO dto = new PlaylistDTO(
                "pid",
                "NewName",
                2L,
                Set.of(songDto)
        );

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(playlist));
        when(userRepo.findById(2L)).thenReturn(Optional.of(newUser));
        when(songRepo.findById("s1")).thenReturn(Optional.of(song));
        when(playlistRepo.save(playlist)).thenReturn(playlist);

        // Act
        PlaylistDTO actual = service.update("pid", dto);

        // Assert
        assertThat(actual.name()).isEqualTo("NewName");
        assertThat(actual.userid()).isEqualTo(2L);
        assertThat(playlist.getSongs()).hasSize(1);
    }


    @Test
    public void sadPath_update_playlistNotFound_throws404() {
        when(playlistRepo.findById("missing")).thenReturn(Optional.empty());

        PlaylistDTO dto = new PlaylistDTO("missing", "Name", null, Set.of());

        assertThatThrownBy(() -> service.update("missing", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    public void sadPath_update_userNotFound_throws404() {
        AppUser oldUser = new AppUser("old", "pw", "USER");
        oldUser.setUserId(1L);

        Playlist existing = new Playlist("Test", oldUser);
        existing.setPlaylistId("pid");

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(existing));
        when(userRepo.findById(2L)).thenReturn(Optional.empty());

        PlaylistDTO dto = new PlaylistDTO("pid", "New", 2L, Set.of());

        assertThatThrownBy(() -> service.update("pid", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    public void sadPath_update_songNotFound_throws404() {
        AppUser user = new AppUser("user", "pw", "USER");
        user.setUserId(1L);

        Playlist playlist = new Playlist("Test", user);
        playlist.setPlaylistId("pid");

        PlaylistDTO dto = new PlaylistDTO("pid", "NewName", 1L,
                Set.of(new SongDTO("missingSong", null, null, null, null, null)));

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(playlist));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(songRepo.findById("missingSong")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("pid", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         PATCH
       ========================================================== */
    @Test
    public void happyPath_patch_updatesOnlyProvidedFields() {
        // Arrange
        AppUser user = new AppUser("john", "pw", "USER");
        user.setUserId(1L);

        Playlist playlist = new Playlist("OldName", user);
        playlist.setPlaylistId("pid");

        PlaylistDTO patchDto = new PlaylistDTO("pid", "PatchedName", null, null);

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(playlist));
        when(playlistRepo.save(playlist)).thenReturn(playlist);

        // Act
        PlaylistDTO actual = service.patch("pid", patchDto);

        // Assert
        assertThat(actual.name()).isEqualTo("PatchedName");
    }

    @Test
    public void sadPath_patch_playlistNotFound_throws404() {
        when(playlistRepo.findById("x")).thenReturn(Optional.empty());

        PlaylistDTO dto = new PlaylistDTO("x", "N", null, null);

        assertThatThrownBy(() -> service.patch("x", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    public void sadPath_patch_userNotFound_throws404() {
        AppUser user = new AppUser("u", "pw", "USER");
        user.setUserId(1L);

        Playlist playlist = new Playlist("Name", user);
        playlist.setPlaylistId("pid");

        PlaylistDTO dto = new PlaylistDTO("pid", "NewName", 99L, null);

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(playlist));
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.patch("pid", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    public void sadPath_patch_songNotFound_throws404() {
        AppUser user = new AppUser("u", "pw", "USER");
        user.setUserId(1L);

        Playlist playlist = new Playlist("Name", user);
        playlist.setPlaylistId("pid");

        PlaylistDTO dto = new PlaylistDTO("pid", null, null,
                Set.of(new SongDTO("missingSong", null, null, null, null, null)));

        when(playlistRepo.findById("pid")).thenReturn(Optional.of(playlist));
        when(songRepo.findById("missingSong")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.patch("pid", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         DELETE
       ========================================================== */
    @Test
    public void happyPath_delete_callsRepoDelete() {
        // Act
        service.delete("abc");

        // Assert
        verify(playlistRepo, times(1)).deleteById("abc");
    }

    /* ==========================================================
         GET BY USER ID AND NAME
       ========================================================== */
    @Test
    public void happyPath_getByUserIdAndName_returnsDTO() {
        AppUser user = new AppUser("john", "pw", "USER");
        user.setUserId(5L);

        Playlist playlist = new Playlist("Chill", user);
        playlist.setPlaylistId("PID");

        when(playlistRepo.findByPlaylistNameAndUser_UserId("Chill", 5L))
                .thenReturn(playlist);

        PlaylistDTO dto = service.getByUserIdAndName(5L, "Chill");

        assertThat(dto.id()).isEqualTo("PID");
        assertThat(dto.name()).isEqualTo("Chill");
    }
}

