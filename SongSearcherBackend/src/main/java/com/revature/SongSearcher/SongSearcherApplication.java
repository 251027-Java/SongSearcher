package com.revature.SongSearcher;

import com.revature.SongSearcher.Controller.DTO.AppUserWOIDDTO;
import com.revature.SongSearcher.Model.*;
import com.revature.SongSearcher.Repository.*;
import com.revature.SongSearcher.Service.AppUserService;
import com.revature.SongSearcher.Service.DatabaseIngestionService;
import com.revature.SongSearcher.Utils.Doc2VecEmbedder;
import com.revature.SongSearcher.Utils.IEmbedder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(SongSearcherApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SongSearcherApplication.class, args);
	}

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner seedData (ArtistRepository artistRepo,
                                AppUserService userService,
                                DatabaseIngestionService ingestionService) {

        return args -> {

            var u1 = new AppUserWOIDDTO("user1", "password1");
            var u2 = new AppUserWOIDDTO("admin", "admin");

            try {
                userService.createUser(u1);
                userService.createAdmin(u2);
            } catch (Exception e) {}

            if (artistRepo.findAll().isEmpty()) {
                log.info("No artists found in Repository. Attempting to seed data from JSON");
                ingestionService.importAlbumsFromJson("./src/main/resources/albums.json");
                log.info("Seed Finished");
            }

        };
    }
}
