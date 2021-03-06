package com.musicloud.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "playlists")
public class Playlist extends BasePlaylist {
    @ManyToOne
    private User user;

    public Playlist() {
        super();
    }

    public Playlist(String name) {
        super(name);
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user= user;
    }
}
