package com.musicloud.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "liked")
public class Liked extends BasePlaylist {
    @OneToOne(optional = false, mappedBy = "liked")
    private User user;


    public Liked() {
        super("Liked songs");
        super.setImageUrl("https://res.cloudinary.com/dtzjbyjzq/image/upload/v1659857847/images/2gkix-gdc9z-001_u08plm_e5vgel.jpg");
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
