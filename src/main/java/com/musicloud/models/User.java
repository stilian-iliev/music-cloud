package com.musicloud.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private Playlist liked;

    @OneToMany
    private List<Playlist> playlists;
    //list of following creators


    private String imageUrl;

    @ManyToMany
    private List<UserRole> roles;
}
