import { get, post, put, del } from "./api.js";

export async function getProfileDto(id) {
    return await get("/api/users/" + id);
}

export async function editProfile(profileDto) {
    return await put("/profile/edit", profileDto);
}

export async function getUserSongs(id) {
    return await get("/api/users/" + id +"/songs");
}

export async function getMyId() {
    return await get("/api/me/");
}

export async function getUserPlaylists(id) {
    return await get('/api/users/' + id + '/playlists');
}

export async function getLiked() {
    return await get(`/api/liked`);
}

export async function createPlaylist(data) {
    return await post(`/api/playlists`, data);
}

export async function likeSong(songId) {
    return await post(`/api/songs/${songId}/like`);
}

export async function dislikeSong(songId) {
    return await del(`/api/songs/${songId}/like`);
}

export async function getPlaylist(id) {
    return await get(`/api/playlists/`+id);
}

export async function addSongToPlaylist(songId, playlistId) {
    return await post(`/api/playlists/${playlistId}/songs/${songId}`);
}

export async function removeSongFromPlaylist(songId, playlistId) {
    return await del(`/api/playlists/${playlistId}/songs/${songId}`);
}

export async function editPlaylist(playlistId, formData) {
    return await put(`/api/playlists/${playlistId}`, formData);
}

export async function editSong(songId, formData) {
    return await put(`/api/songs/${songId}`, formData);
}

export async function deleteSong(songId) {
    return await del(`/api/songs/${songId}`);
}

export async function deletePlaylist(playlistId) {
    return await del(`/api/playlists/${playlistId}`);
}

export async function getFollowingPlaylists() {
    return await get('/api/playlists/following');
}

export async function followPlaylist(playlistId) {
    return await post(`/api/playlists/${playlistId}/follow`);
}

export async function unfollowPlaylist(playlistId) {
    return await del(`/api/playlists/${playlistId}/follow`);
}

export async function isPlaylistFollowed(playlistId) {
    return await get(`/api/follow/playlist?id=${playlistId}`);
}

export async function getFollowingUsers() {
    return await get('/api/users/following');
}

export async function followUser(userId) {
    return await post(`/api/users/${userId}/follow`);
}

export async function unfollowUser(userId) {
    return await del(`/api/users/${userId}/follow`);
}

export async function isUserFollowed(userId) {
    return await get(`/api/users/${userId}/follow`);
}

export async function getAllSongs(query) {
    return await get(`/api/songs${query ? `?q=${query}` : ''}`);
}

export async function getAllPlaylists(query) {
    return await get(`/api/playlists${query ? `?q=${query}` : ''}`);
}

export async function getAllUsers(query) {
    return await get(`/api/users${query ? `?q=${query}` : ''}`);
}