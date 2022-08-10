package com.musicloud.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Song> songs;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Liked liked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    private String imageUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    private LocalDateTime creationDate;

    @ManyToMany
    private Set<Playlist> followedPlaylists;

    @ManyToMany
    private Set<User> followedUsers;

    @ManyToMany(mappedBy = "followedUsers")
    private Set<User> followers;


    public User() {
        this.roles = new HashSet<>();
        this.creationDate = LocalDateTime.now();
        this.playlists = new ArrayList<>();
        this.followedPlaylists = new LinkedHashSet<>();
        this.followedUsers = new LinkedHashSet<>();
        this.followers = new LinkedHashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BasePlaylist getLiked() {
        return liked;
    }

    public void setLiked(Liked liked) {
        this.liked = liked;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> basePlaylists) {
        this.playlists = basePlaylists;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public void setFollowedPlaylists(Set<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    public Set<User> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(Set<User> followedUsers) {
        this.followedUsers = followedUsers;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }


    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName != null ? firstName : "");
        if (!sb.isEmpty()) sb.append(" ");
        sb.append(lastName != null ? lastName : "");
        if (sb.isEmpty()) sb.append(username);
        return sb.toString();
    }

    public void addRole(UserRole role) {
        roles.add(role);
    }

    public void removeRole(UserRole role) {
        roles.remove(role);
    }

    public void followPlaylist(Playlist playlist) {
        followedPlaylists.add(playlist);
    }

    public void unfollowPlaylist(Playlist playlist) {
        followedPlaylists.remove(playlist);
    }

    public boolean isFollowingPlaylist(UUID playlistId) {
        return getFollowedPlaylists().stream().anyMatch(p -> p.getId().equals(playlistId));
    }

    public void followUser(User user) {
        followedUsers.add(user);
    }

    public void unfollowUser(User user) {
        followedUsers.remove(user);
    }

    public boolean isFollowingUser(UUID userId) {
        return getFollowedUsers().stream().anyMatch(u -> u.getId().equals(userId));
    }
}
