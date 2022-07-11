import { get, post, put } from "./api.js";

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

export async function isLiked(songId) {
    return await get(host + `/api/song/liked?song=${songId}`);
}

export async function getLiked() {
    return await get(host + `/api/liked`);
}

export async function createPlaylist(data) {
    return await post(host + `/playlists/create`, data);
}