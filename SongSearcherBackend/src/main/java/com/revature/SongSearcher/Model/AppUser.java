package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
@Data
@NoArgsConstructor
public class AppUser {

    @Id @GeneratedValue
    private long user_id;

    @Column(unique = true)
    private String user_name;

    @Column(nullable = false)
    private String user_password;

    private String user_role;

    @OneToMany(mappedBy = "user")
    private List<Playlist> userPlaylists = new ArrayList<>();

    public AppUser (String name, String password, String role) {
        this.user_name = name;
        this.user_password = password;
        this.user_role = role;
    }
}
