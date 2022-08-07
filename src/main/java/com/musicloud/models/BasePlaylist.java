package com.musicloud.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@MappedSuperclass
public abstract class BasePlaylist {
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
    private Set<Song> songs;

    private String imageUrl;

    private LocalDateTime creationTime;

    public BasePlaylist() {
        songs = new LinkedHashSet<>();
        creationTime = LocalDateTime.now();
    }

    public BasePlaylist(String name) {
        this();
        this.name = name;
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

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    abstract public User getUser();

    abstract public void setUser(User user);

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public boolean containsId(UUID songId) {
        return songs.stream().anyMatch(s -> s.getId() == songId);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
}
