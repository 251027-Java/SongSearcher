package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.*;
import com.revature.SongSearcher.Model.*;
import com.revature.SongSearcher.Repository.AlbumRepository;
import com.revature.SongSearcher.Repository.SongRepository;
import com.revature.SongSearcher.Utils.IEmbedder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SongServiceTests {

    @Mock
    private SongRepository songRepo;

    @Mock
    private AlbumRepository albumRepo;

    @Mock
    private IEmbedder embedder;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private SongService service;
    private Artist artist;
    private Album album;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        artist = new Artist("artistId-123", "Test Artist");
        album = new Album("a1","AlbumTitle", 2024, Set.of(artist));

        song = new Song();
        song.setSongId("s1");
        song.setTitle("SongTitle");
        song.setLength(new BigDecimal("3.50"));
        song.setLyrics("lyrics...");
        song.setAlbum(album);
        song.setArtists(Set.of(artist));

        album.getAlbumSongs().add(song);
        artist.getSongs().add(song);
    }

    @Test
    void happyPath_searchByTitle_returnsDTOs() {
        when(songRepo.findByTitleContainingIgnoreCase("SongTitle")).thenReturn(List.of(song));

        List<SongDTO> results = service.searchByTitle("SongTitle");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo("s1");
    }

    @Test
    void happyPath_searchByAlbum_returnsDTOs() {
        when(songRepo.findByAlbum_TitleContainingIgnoreCase("AlbumTitle")).thenReturn(List.of(song));

        List<SongDTO> results = service.searchByAlbum("AlbumTitle");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).album().id()).isEqualTo("a1");
    }

    @Test
    void happyPath_searchByArtist_returnsDTOs() {
        when(songRepo.findSongsByArtistName("Test Artist")).thenReturn(List.of(song));

        List<SongDTO> results = service.searchByArtist("Test Artist");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).artists()).isNotEmpty();
    }

    @Test
    void happyPath_getUserSongRecommendations_returnsRecommendations() {
        PlaylistDTO favorites = mock(PlaylistDTO.class);
        SongDTO favDto = new SongDTO(
                "sFav",
                "FavSong",
                new BigDecimal("3.00"),
                "fav lyrics",
                new AlbumSlimDTO("a1", "AlbumTitle", 2024),
                List.of(new ArtistDTO("ar1", "Artist One"))
        );

        when(favorites.songs()).thenReturn(Set.of(favDto));
        when(playlistService.getByUserIdAndName(123L, "Favorites")).thenReturn(favorites);
        when(albumRepo.findById("a1")).thenReturn(Optional.of(album));
        when(embedder.getEmbedding("fav lyrics")).thenReturn(new float[]{0.5f, 0.5f});
        when(songRepo.recommend(any(float[].class), anyList(), eq(10))).thenReturn(List.of(song));

        List<SongDTO> recs = service.getUserSongRecommendations(123L);

        assertThat(recs).hasSize(1);
        assertThat(recs.get(0).id()).isEqualTo("s1");
        verify(playlistService).getByUserIdAndName(123L, "Favorites");
    }

    @Test
    public void edgeCase_getUserSongRecommendations_noFavorites_returnsEmptyList() {
        PlaylistDTO favorites = mock(PlaylistDTO.class);
        when(favorites.songs()).thenReturn(Set.of());
        when(playlistService.getByUserIdAndName(999L, "Favorites")).thenReturn(favorites);

        List<SongDTO> actual = service.getUserSongRecommendations(999L);

        assertThat(actual).isEmpty();
    }

    @Test
    public void sadPath_getUserSongRecommendations_mismatchedEmbeddingLengths_throwsIllegalArgumentException() {
        PlaylistDTO favorites = mock(PlaylistDTO.class);
        SongDTO dto1 = new SongDTO(
                "sA",
                "A",
                new BigDecimal("2.00"),
                "lyrics1",
                new AlbumSlimDTO("a1", "AlbumTitle", 2024),
                List.of()
        );
        SongDTO dto2 = new SongDTO(
                "sB",
                "B",
                new BigDecimal("2.50"),
                "lyrics2",
                new AlbumSlimDTO("a1", "AlbumTitle", 2024),
                List.of()
        );

        when(favorites.songs()).thenReturn(Set.of(dto1, dto2));
        when(playlistService.getByUserIdAndName(55L, "Favorites")).thenReturn(favorites);
        when(albumRepo.findById("a1")).thenReturn(Optional.of(album));
        when(embedder.getEmbedding("lyrics1")).thenReturn(new float[]{1f, 2f});
        when(embedder.getEmbedding("lyrics2")).thenReturn(new float[]{1f, 2f, 3f});

        assertThatThrownBy(() -> service.getUserSongRecommendations(55L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void happyPath_getAll_returnsSongDTOs() {
        when(songRepo.findAll()).thenReturn(List.of(song));

        List<SongDTO> results = service.getAll();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo("s1");
    }

    @Test
    public void edgeCase_getAll_empty_returnsEmptyList() {
        // Arrange
        when(songRepo.findAll()).thenReturn(List.of());

        // Act
        List<SongDTO> actual = service.getAll();

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void happyPath_getById_existingSong_returnsDTO() {
        when(songRepo.findById("s1")).thenReturn(Optional.of(song));

        SongDTO dto = service.getById("s1");

        assertThat(dto.id()).isEqualTo("s1");
        assertThat(dto.title()).isEqualTo("SongTitle");
    }

    @Test
    public void sadPath_getSongById_notFound_throwsNotFound() {
        // Arrange
        when(songRepo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getById("missing"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void happyPath_getById_nonExistingSong_throwsException() {
        when(songRepo.findById("s1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("s1"))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void happyPath_create_validSongWOIDDTO_returnsSongDTO() {
        // Arrange
        AlbumSlimDTO albumDto = new AlbumSlimDTO("a1", "Greatest Hits", 2024);
        ArtistDTO artistDto = new ArtistDTO("ar1", "Artist One");
        Set<ArtistDTO> artists = new HashSet<>();
        artists.add(artistDto);

        SongWOIDDTO dto = new SongWOIDDTO(
                "My New Song",
                new BigDecimal("3.45"),
                "These are the lyrics of my new song",
                albumDto,
                artists.stream().toList()
        );

        Album album = new Album("a1", "Greatest Hits", 2024, new HashSet<>());
        Song savedSong = new Song("s1", "My New Song", new BigDecimal("3.45"),
                album, Set.of(new Artist("ar1", "Artist One")),
                "These are the lyrics of my new song",
                new float[]{0.1f, 0.2f});

        when(albumRepo.findById("a1")).thenReturn(Optional.of(album));
        when(embedder.getEmbedding(dto.lyrics())).thenReturn(new float[]{0.1f, 0.2f});
        when(songRepo.save(any(Song.class))).thenReturn(savedSong);

        // Act
        SongDTO created = service.create(dto);

        // Assert
        assertThat(created.title()).isEqualTo("My New Song");
        assertThat(created.album().id()).isEqualTo("a1");
        assertThat(created.artists()).hasSize(1);
        verify(songRepo).save(any(Song.class));
        verify(embedder).getEmbedding("These are the lyrics of my new song");
    }

    @Test
    public void sadPath_createSong_albumMissing_throwsNotFound() {
        // Arrange
        AlbumSlimDTO albumDto = new AlbumSlimDTO("a1", "Greatest Hits", 2024);
        ArtistDTO artistDto = new ArtistDTO("ar1", "Artist One");
        Set<ArtistDTO> artists = new HashSet<>();
        artists.add(artistDto);

        SongWOIDDTO dto = new SongWOIDDTO(
                "Fail Song",
                new BigDecimal("1.00"),
                "lyrics",
                new AlbumSlimDTO("missing", "?", 1990),
                artists.stream().toList()
        );

        when(albumRepo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void happyPath_update_existingSong_updatesFields() {
        SongDTO dto = new SongDTO(
                "s1",
                "UpdatedTitle",
                new BigDecimal("4.00"),
                "new lyrics",
                new AlbumSlimDTO("a1", "AlbumTitle", 2024),
                List.of()
        );

        when(songRepo.findById("s1")).thenReturn(Optional.of(song));
        when(albumRepo.findById("a1")).thenReturn(Optional.of(album));
        when(songRepo.save(song)).thenReturn(song);

        SongDTO updated = service.update("s1", dto);

        assertThat(updated.title()).isEqualTo("UpdatedTitle");
        verify(songRepo).save(song);
    }

    @Test
    public void sadPath_update_songMissing_throwsNotFound() {
        // Arrange
        when(songRepo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() ->
                service.update("missing", mock(SongDTO.class))
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void sadPath_update_albumMissing_throwsNotFound() {
        // Arrange
        when(songRepo.findById("songId-123")).thenReturn(Optional.of(song));
        when(albumRepo.findById(anyString())).thenReturn(Optional.empty());

        SongDTO dto = new SongDTO(
                "songId-123",
                "Title",
                BigDecimal.ONE,
                "lyrics",
                new AlbumSlimDTO("missing", "?", 0),
                null
        );

        // Act + Assert
        assertThatThrownBy(() -> service.update("songId-123", dto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void happyPath_patch_partialUpdate_updatesOnlyProvidedFields() {
        SongDTO dto = new SongDTO(
                "s1",
                "PatchedTitle",
                null,
                null,
                null,
                null
        );

        when(songRepo.findById("s1")).thenReturn(Optional.of(song));
        when(songRepo.save(song)).thenReturn(song);

        SongDTO patched = service.patch("s1", dto);

        assertThat(patched.title()).isEqualTo("PatchedTitle");
        verify(songRepo).save(song);
    }

    @Test
    public void sadPath_patch_songMissing_throwsNotFound() {
        when(songRepo.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.patch("missing", mock(SongDTO.class))
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void happyPath_delete_callsRepository() {
        service.delete("s1");
        verify(songRepo).deleteById("s1");
    }

    @Test
    void happyPath_searchByLyrics_returnsDTOs() {
        SearchDTO search = new SearchDTO("some lyrics");

        when(embedder.getEmbedding("some lyrics")).thenReturn(new float[]{1f, 2f});
        when(songRepo.findMostSimilar(new float[]{1f, 2f}, 10)).thenReturn(List.of(song));

        List<SongDTO> results = service.searchByLyrics(search);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo("s1");
    }

    @Test
    public void edgeCase_searchByLyrics_noResults_returnsEmptyList() {
        when(embedder.getEmbedding(anyString())).thenReturn(new float[]{1,1,1});
        when(songRepo.findMostSimilar(any(float[].class), eq(10))).thenReturn(List.of());

        List<SongDTO> actual = service.searchByLyrics(new SearchDTO("nothing"));

        assertThat(actual).isEmpty();
    }

}
