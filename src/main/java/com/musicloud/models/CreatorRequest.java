package com.musicloud.models;

import javax.persistence.*;

@Entity
@Table(name = "requests")
public class CreatorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private User user;


}
