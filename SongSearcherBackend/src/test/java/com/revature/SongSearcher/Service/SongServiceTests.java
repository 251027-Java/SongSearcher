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

    private Album album;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        album = new Album();
        album.setAlbumId("a1");
        album.setTitle("AlbumTitle");
        album.setReleaseyear(2024);
        album.setArtists(new HashSet<>());

        song = new Song();
        song.setSongId("s1");
        song.setTitle("SongTitle");
        song.setLength(new BigDecimal("3.50"));
        song.setLyrics("lyrics...");
        song.setAlbum(album);
        song.setArtists(new HashSet<>());
    }

    @Test
    void happyPath_getAll_returnsSongDTOs() {
        when(songRepo.findAll()).thenReturn(List.of(song));

        List<SongDTO> results = service.getAll();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo("s1");
    }

    @Test
    void happyPath_getById_existingSong_returnsDTO() {
        when(songRepo.findById("s1")).thenReturn(Optional.of(song));

        SongDTO dto = service.getById("s1");

        assertThat(dto.id()).isEqualTo("s1");
        assertThat(dto.title()).isEqualTo("SongTitle");
    }

    @Test
    void happyPath_getById_nonExistingSong_throwsException() {
        when(songRepo.findById("s1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById("s1"))
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


}
