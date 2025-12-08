package com.revature.SongSearcher;

import com.revature.SongSearcher.Model.*;
import com.revature.SongSearcher.Repository.*;
import com.revature.SongSearcher.Utils.IEmbedder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SongSearcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongSearcherApplication.class, args);
	}

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner seedData (ArtistRepository artistRepo,
                                      AlbumRepository albumRepo,
                                      SongRepository songRepo,
                                      PlaylistRepository playlistRepo,
                                      AppUserRepository userRepo,
                                      IEmbedder embedder, PasswordEncoder encoder) {

        return args -> {

            var u1 = new AppUser("user1", encoder.encode("password1"), "USER");
            var u2 = new AppUser("admin",  encoder.encode("password2"), "ADMIN");

            try {
                userRepo.saveAll(List.of(u1, u2));
            } catch (Exception e) {
                System.out.println("Failed to insert users");
                u1 = userRepo.findByUsername("user1").orElseThrow(() -> new Exception("Unable to find test user"));
            }

            var a1 = new Artist("Lady Gaga");
            var a2 = new Artist( "Bruno Mars");

            var ab1 = new Album("Mayhem", 2025, Set.of(a1));

            String l = "These are the lyrics";
            var s1 = new Song("Die With a Smile",
                    new BigDecimal("433"), ab1, Set.of(a2), l,
                    embedder.getEmbedding(l));

            l = "These are some new lyrics that are probably not as similar";
            var s2 = new Song("Abracadabra", new BigDecimal("400"), ab1,
                    Set.of(), l, embedder.getEmbedding(l));

            var p1 = new Playlist("Favorites", u1);
            p1.setSongs(Set.of(s1));

            try {
                artistRepo.saveAll(List.of(a1, a2));
                albumRepo.save(ab1);
                songRepo.saveAll(List.of(s1, s2));
                playlistRepo.save(p1);
            } catch (Exception e) {
                System.out.println("Failed to insert information");
                e.printStackTrace();
            }
        };
    }
}
