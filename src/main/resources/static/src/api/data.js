import { get, post } from "./api.js";

const host = 'http://localhost:8080';

export async function getProfileDto(id) {
    return await get(host + "/api/users/" + id);
}

export async function editProfile(profileDto) {
    console.log('editing');

    return await post(host + "/profile/edit", profileDto);
}