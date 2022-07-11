package com.musicloud.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID id;


    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Song> songs;

    private String imageUrl;

    @ManyToOne
    private User user;

    public Playlist() {
        songs = new ArrayList<>();
    }

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.imageUrl = "https://res.cloudinary.com/dtzjbyjzq/image/upload/v1657567072/images/1482975da7275050a3a8406f90c4610d_f9qkkc.jpg";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean containsId(UUID songId) {
        return songs.stream().anyMatch(s -> s.getId() == songId);
    }
}
