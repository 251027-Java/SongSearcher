package com.revature.SongSearcher.Service;

import com.revature.SongSearcher.Controller.DTO.ArtistDTO;
import com.revature.SongSearcher.Controller.DTO.ArtistWOIDDTO;
import com.revature.SongSearcher.Model.Artist;
import com.revature.SongSearcher.Repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ArtistServiceTests {

    @Mock
    private ArtistRepository repo;

    @InjectMocks
    private ArtistService service;

    /* ==========================================================
         GET ALL
       ========================================================== */

    @Test
    public void happyPath_getAll_returnsListOfArtistDTOs() {
        // Arrange
        Artist a1 = new Artist("id1", "Linkin Park");
        Artist a2 = new Artist("id2", "Imagine Dragons");

        when(repo.findAll()).thenReturn(List.of(a1, a2));

        // Act
        List<ArtistDTO> actual = service.getAll();

        // Assert
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0)).isEqualTo(new ArtistDTO("id1", "Linkin Park"));
        assertThat(actual.get(1)).isEqualTo(new ArtistDTO("id2", "Imagine Dragons"));
    }

    /* ==========================================================
         GET BY ID
       ========================================================== */

    @Test
    public void happyPath_getArtistById_returnsArtistDTO() {
        // Arrange
        Artist a = new Artist("id123", "Foo Fighters");
        when(repo.findById("id123")).thenReturn(Optional.of(a));

        ArtistDTO expected = new ArtistDTO("id123", "Foo Fighters");

        // Act
        ArtistDTO actual = service.getById("id123");

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void sadPath_getArtistById_notFound_throws404() {
        // Arrange
        when(repo.findById("missing")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getById("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         CREATE
       ========================================================== */

    @Test
    public void happyPath_createArtist_returnsArtistDTO() {
        // Arrange
        ArtistWOIDDTO dto = new ArtistWOIDDTO("Adele");

        Artist saved = new Artist("id321", "Adele");
        when(repo.save(any(Artist.class))).thenReturn(saved);

        ArtistDTO expected = new ArtistDTO("id321", "Adele");

        // Act
        ArtistDTO actual = service.create(dto);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    /* ==========================================================
         UPDATE
       ========================================================== */

    @Test
    public void happyPath_updateArtist_updatesNameAndReturnsDTO() {
        // Arrange
        Artist existing = new Artist("abc123", "Old Name");
        when(repo.findById("abc123")).thenReturn(Optional.of(existing));

        ArtistDTO updateDto = new ArtistDTO("abc123", "New Name");

        Artist saved = new Artist("abc123", "New Name");
        when(repo.save(existing)).thenReturn(saved);

        ArtistDTO expected = new ArtistDTO("abc123", "New Name");

        // Act
        ArtistDTO actual = service.update("abc123", updateDto);

        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(existing.getName()).isEqualTo("New Name");
    }

    @Test
    public void sadPath_updateArtist_notFound_throws404() {
        // Arrange
        when(repo.findById("unknown")).thenReturn(Optional.empty());
        ArtistDTO dto = new ArtistDTO("unknown", "Does Not Matter");

        // Act + Assert
        assertThatThrownBy(() -> service.update("unknown", dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         PATCH
       ========================================================== */

    @Test
    public void happyPath_patchArtist_updatesOnlyName() {
        // Arrange
        Artist existing = new Artist("abc123", "Original Name");
        when(repo.findById("abc123")).thenReturn(Optional.of(existing));

        ArtistDTO patchDto = new ArtistDTO("abc123", "Patched Name");

        Artist saved = new Artist("abc123", "Patched Name");
        when(repo.save(existing)).thenReturn(saved);

        ArtistDTO expected = new ArtistDTO("abc123", "Patched Name");

        // Act
        ArtistDTO actual = service.patch("abc123", patchDto);

        // Assert
        assertThat(actual).isEqualTo(expected);
        assertThat(existing.getName()).isEqualTo("Patched Name");
    }

    @Test
    public void sadPath_patchArtist_notFound_throws404() {
        // Arrange
        when(repo.findById("missing")).thenReturn(Optional.empty());
        ArtistDTO patchDto = new ArtistDTO("missing", "New Name");

        // Act + Assert
        assertThatThrownBy(() -> service.patch("missing", patchDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    /* ==========================================================
         DELETE
       ========================================================== */

    @Test
    public void happyPath_deleteArtist_callsRepoDelete() {
        // Arrange
        String id = "deleteMe";

        // Act
        service.delete(id);

        // Assert
        verify(repo, times(1)).deleteById(id);
    }
}

