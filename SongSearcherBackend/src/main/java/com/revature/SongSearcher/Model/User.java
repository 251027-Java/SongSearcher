package com.revature.SongSearcher.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue
    private String user_id;

    private String user_email;

    private String user_password;

    @OneToMany(mappedBy = "user")
    private List<Playlist> userPlaylists = new ArrayList<>();

    public User (String email, String password) {
        this.user_email = email;
        this.user_password = password;
    }
}
