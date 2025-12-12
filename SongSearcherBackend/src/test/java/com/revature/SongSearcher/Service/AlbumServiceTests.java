package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.AlbumDTO;
import com.revature.SongSearcher.Controller.DTO.AlbumWOIDDTO;
import com.revature.SongSearcher.Controller.DTO.ArtistDTO;
import com.revature.SongSearcher.Model.Album;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTests {
    @Mock
    private AlbumRepository repo;

    @InjectMocks
    private AlbumService service;

    @Test
    public void happyPath_getAll_returnsListOfAlbumDTOs() {
        // Arrange
        Artist art = new Artist("a1", "Artist One");
        Album a = new Album("id1", "Title1", 2001, Set.of(art));

        when(repo.findAll()).thenReturn(List.of(a));

        AlbumDTO expected = new AlbumDTO("id1", "Title1", 2001,
                List.of(new ArtistDTO("a1", "Artist One")));

        // Act
        List<AlbumDTO> actual = service.getAll();

        // Assert
        assertThat(actual).containsExactly(expected);
    }

    @Test
    public void happyPath_getById_returnsAlbumDTO() {
        // Arrange
        Artist art = new Artist("a1", "Artist One");
        Album album = new Album("id1", "AlbumTitle", 1999, Set.of(art));

        when(repo.findById("id1")).thenReturn(Optional.of(album));

        AlbumDTO expected = new AlbumDTO("id1", "AlbumTitle", 1999,
                List.of(new ArtistDTO("a1", "Artist One")));

        // Act
        AlbumDTO actual = service.getById("id1");

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void sadPath_getById_throwsNotFound() {
        // Arrange
        when(repo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResponseStatusException.class, () -> service.getById("missing"));
    }

    @Test
    public void happyPath_create_savesAndReturnsAlbumDTO() {
        // Arrange
        ArtistDTO aDto = new ArtistDTO("a1", "Artist One");
        AlbumWOIDDTO dto = new AlbumWOIDDTO("New Album", 2024, List.of(aDto));

        Artist art = new Artist("a1", "Artist One");
        Album expectedEntity = new Album("generatedId", "New Album", 2024, Set.of(art));

        when(repo.save(any(Album.class))).thenReturn(expectedEntity);

        AlbumDTO expected = new AlbumDTO(
                "generatedId",
                "New Album",
                2024,
                List.of(aDto)
        );

        // Act
        AlbumDTO actual = service.create(dto);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void sadPath_create_artistMissing_throwsException() {
        // Arrange
        AlbumWOIDDTO dto = new AlbumWOIDDTO("Album", 2020, List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class, () -> service.create(dto));
    }

    @Test
    public void happyPath_update_overwritesAlbumAndReturnsUpdatedDTO() {
        // Arrange
        String id = "album-123";

        // Existing entity (in “database”)
        Artist existingArtist = new Artist("artist-1", "Old Artist");
        Set<Artist> existingArtists = new HashSet<>();
        existingArtists.add(existingArtist);

        Album existingAlbum = new Album(id, "Old Title", 1990, existingArtists);

        when(repo.findById(id)).thenReturn(Optional.of(existingAlbum));

        // NEW DTO data for updating
        ArtistDTO newArtistDTO = new ArtistDTO("artist-2", "New Artist");

        // *** IMPORTANT FIX ***
        // Wrap DTO artists in a mutable list
        List<ArtistDTO> newArtistDTOList =
                new ArrayList<>(List.of(newArtistDTO));

        AlbumDTO updateDTO =
                new AlbumDTO(id, "New Title", 2024, newArtistDTOList);

        // Expected result
        AlbumDTO expectedDTO =
                new AlbumDTO(id, "New Title", 2024, newArtistDTOList);

        // Mock saving behavior
        when(repo.save(any(Album.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AlbumDTO actual = service.update(id, updateDTO);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedDTO);

        // Verify the old artists were cleared
        assertThat(existingAlbum.getArtists().size()).isEqualTo(1);

        // Verify new artist inserted
        Artist savedArtist = existingAlbum.getArtists().iterator().next();
        assertThat(savedArtist.getName()).isEqualTo("New Artist");
    }

    @Test
    public void sadPath_update_albumNotFound_throwsNotFound() {
        // Arrange
        when(repo.findById("missing")).thenReturn(Optional.empty());

        AlbumDTO dto = new AlbumDTO("missing", "DoesntMatter", 2000, List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class, () -> service.update("missing", dto));
    }

    @Test
    public void happyPath_patch_updatesOnlyProvidedFields() {
        // Arrange
        Artist oldArtist = new Artist("a1", "Old Artist");
        Album existing = new Album("id1", "Old Title", 2000, Set.of(oldArtist));

        when(repo.findById("id1")).thenReturn(Optional.of(existing));

        AlbumDTO patch = new AlbumDTO("id1", "Patched Title", 0, List.of());

        Album saved = new Album("id1", "Patched Title", 2000, Set.of(oldArtist));
        when(repo.save(any(Album.class))).thenReturn(saved);

        AlbumDTO expected = new AlbumDTO("id1", "Patched Title", 2000,
                List.of(new ArtistDTO("a1", "Old Artist")));

        // Act
        AlbumDTO actual = service.patch("id1", patch);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void sadPath_patch_albumNotFound_throwsNotFound() {
        // Arrange
        when(repo.findById("missing")).thenReturn(Optional.empty());

        AlbumDTO dto = new AlbumDTO("missing", "SomeTitle", 2000, List.of());

        // Act + Assert
        assertThrows(ResponseStatusException.class, () -> service.patch("missing", dto));
    }

    @Test
    public void happyPath_delete_invokesRepositoryDelete() {
        // Act
        service.delete("id1");

        // Assert
        verify(repo, times(1)).deleteById("id1");
    }

}


