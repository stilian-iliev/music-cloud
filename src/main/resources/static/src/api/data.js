import { get, post, put, del } from "./api.js";

const host = 'http://localhost:8080';

export async function getProfileDto(id) {
    return await get(host + "/api/users/" + id);
}

export async function editProfile(profileDto) {
    return await put(host + "/profile/edit", profileDto);
}

export async function getUserSongs(id) {
    return await get(host + "/api/users/" + id +"/songs");
}

export async function getMyId() {
    return await get(host + "/api/me/");
}

export async function getUserPlaylists(id) {
    return await get(host + '/api/users/' + id + '/playlists');
}


export async function getLiked() {
    return await get(host + `/api/liked`);
}

export async function createPlaylist(data) {
    return await post(host + `/playlists/create`, data);
}

export async function likeSong(songId) {
    return await post(host + `/api/song/like?id=${songId}`);
}

export async function dislikeSong(songId) {
    return await post(host + `/api/song/dislike?id=${songId}`);
}

export async function getPlaylist(id) {
    return await get(host + `/api/playlists/`+id);
}

export async function addSongToPlaylist(songId, playlistId) {
    return await post(host + `/api/playlists/${playlistId}/add?id=${songId}`);
}

export async function removeSongFromPlaylist(songId, playlistId) {
    return await del(host + `/api/playlists/${playlistId}/remove?id=${songId}`);
}

export async function editPlaylist(playlistId, formData) {
    return await put(host + `/api/playlists/${playlistId}/edit`, formData);
}