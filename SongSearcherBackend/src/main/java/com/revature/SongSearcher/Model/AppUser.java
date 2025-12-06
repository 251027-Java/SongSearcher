package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id @GeneratedValue
    private long user_id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String userpassword;

    private String user_role;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Playlist> userPlaylists = new ArrayList<>();

    public AppUser (String name, String password, String role) {
        this.username = name;
        this.userpassword = password;
        this.user_role = role;
    }
}
