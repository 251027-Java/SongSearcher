package com.revature.SongSearcher.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.revature.SongSearcher.Controller.DTO.*;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseIngestionService {

    private final ObjectMapper objectMapper;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final Gson gson;

    public DatabaseIngestionService(
            ObjectMapper objectMapper,
            ArtistService artistService,
            AlbumService albumService,
            SongService songService) {
        this.objectMapper = objectMapper;
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
        this.gson = new Gson();
    }

    public void importAlbumsFromJson(String jsonFilePath) throws FileNotFoundException {
        // Read JSON file
        FileReader reader = new FileReader(jsonFilePath);
        Type listExpenseType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> albumsJson = gson.fromJson(reader, listExpenseType);

        for (Map<String, Object> albumMap : albumsJson) {
            try {
                processAlbum(albumMap);
            } catch (Exception e) {
                System.err.println("Skipping album due to error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void processAlbum(Map<String, Object> albumMap) {
        // Extract album fields

        String albumTitle = (String) albumMap.get("album_name");
        Integer releaseYear = Integer.parseInt( albumMap.get("release_date").toString().split("-")[0]);

        List<String> albumArtists = (List<String>) albumMap.get("album_artists");

        if (albumTitle == null || albumArtists == null || albumArtists.isEmpty()) {
            System.err.println("Album missing required fields. Skipping.");
            return;
        }

        // Save artists first
        List<ArtistWOIDDTO> albumArtistsDTOs = albumArtists.stream()
                .map(ArtistWOIDDTO::new)
                .toList();

        List<ArtistDTO> savedAlbumArtists = new ArrayList<>();
        for (ArtistWOIDDTO dto : albumArtistsDTOs) {
            ArtistDTO artist = artistService.getByName(dto.name());

            if (artist == null) {
                artist =  artistService.create(dto);
            }

            savedAlbumArtists.add(artist);
        }

        AlbumWOIDDTO albumdtonew = new AlbumWOIDDTO(albumTitle, releaseYear, savedAlbumArtists);

        AlbumDTO albumdto = albumService.getByTitleAndReleaseYear(albumTitle, releaseYear);
        if (albumdto == null) {
            albumdto = albumService.create(albumdtonew);
        }

        AlbumSlimDTO slimmedAlbum = new AlbumSlimDTO(albumdto.id(), albumdto.title(), albumdto.releaseYear());

        // Process tracks
        List<Map<String, Object>> tracksJson = (List<Map<String, Object>>) albumMap.get("tracks");
        if (tracksJson == null || tracksJson.isEmpty()) return;

        for (Map<String, Object> trackMap : tracksJson) {
            String trackTitle = (String) trackMap.get("track_name");
            BigDecimal durationMs = new BigDecimal(trackMap.get("duration_ms").toString());
            String lyrics = (String) trackMap.get("lyrics");

            List<String> trackArtistsJson = (List<String>) trackMap.get("additional_artists");
            if (trackTitle == null || durationMs == null || lyrics == null) {
                System.err.println("Skipping track due to missing fields: " + trackTitle);
                continue;
            }

            List<ArtistWOIDDTO> songArtistsDTOs = trackArtistsJson.stream()
                    .map(ArtistWOIDDTO::new)
                    .toList();

            List<ArtistDTO> songArtists = new ArrayList<>();
            for (ArtistWOIDDTO dto : songArtistsDTOs) {
                ArtistDTO artist = artistService.getByName(dto.name());

                if (artist == null) {
                    artist =  artistService.create(dto);
                }

                songArtists.add(artist);
                }

            SongWOIDDTO songdto = new SongWOIDDTO(trackTitle, durationMs, lyrics, slimmedAlbum, songArtists);

            artistService.flush();
            albumService.flush();
            SongDTO savedSong = songService.create(songdto);
        }
    }
}