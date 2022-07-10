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