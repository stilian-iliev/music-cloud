
import { queueList, isPlaying } from './audioplayer.js'
import { html } from '../../../node_modules/lit-html/lit-html.js';
import {likeSong, dislikeSong} from '../../api/data.js';

let songList;
let trackNumber;
let userLikedSongs;
let myId;
let playlistCratorId;
let loc;
export const songListTemplate = (songs, liked, me, pci) => {
    document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
    loc = window.location.pathname.split('/')[1];
    myId = me;
    playlistCratorId = pci;
    songList = songs;
    userLikedSongs = liked.map(s => s.id);
    trackNumber = 0;
    return html`
<section class="pb-4">
            <table id="current-playlist">
                <thead>
                <tr>
                    <th>
                    </th>
                    <th>Song</th>
                    <th>Artist</th>
                    <th></th>
                </tr>
                </thead>
                
                <tbody>
                
                ${songs.map(songPreview)}

                </tbody>
                </table>
            
        </section>
<div class="modal top fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
<div class="modal-dialog  ">
    <div class="modal-content">
    <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
    </div>
    <div class="modal-body">...</div>
    <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-mdb-dismiss="modal">
        Close
        </button>
        <button type="button" class="btn btn-primary">Save changes</button>
    </div>
    </div>
</div>
</div>
`;}

const songPreview = (song) => {
    trackNumber++;
    return html`
    <tr id="${song.id}" class="song ${isPlaying(song.id) ? 'current' : ''}" data-track-number="${trackNumber}" data-source="Heavydirtysoul.mp3">
    <td @click=${onPlay} class="playTrack"><span class="track-number">${trackNumber}</span></td>
    <td width="65%"><cite class="title">${song.title}</cite></td>
    <td width="35%">
        <address><a rel="artist" href="${'/user/'+song.creator.id}">${song.creator.username}</a></address>
    </td>
    <td @click=${onLikeDislike}>${userLikedSongs.includes(song.id) ? html`<i class="fas fa-heart px-2"></i>` : html`<i class="far fa-heart px-2"></i>`}<i id="dropdownMenuButton"
    data-mdb-toggle="dropdown"
    aria-expanded="false" class="fas fa-ellipsis-v px-2"></i><ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
    ${myId == song.creator.id ? html`<li><a class="dropdown-item" href="#">Edit song</a></li>` : ""}
    <li><a class="dropdown-item" href="#" data-mdb-toggle="modal" data-mdb-target="#exampleModal">Add to playlist</a></li>
    ${loc == "playlist" && myId == playlistCratorId ? html`<li><a class="dropdown-item" href="#">Remove from playlist</a></li>` : ''}
  </ul><span class="runtime">${getTimeCodeFromNum(song.duration)}</span></td>
    </tr>
`;}

function onPlay(e) {
    let el = e.target.parentElement.parentElement;
    if (el.tagName == 'TR' && e.target.parentElement.classList.contains('playTrack')) {
        queueList(songList, el.getAttribute('data-track-number'));
        selectSong(el.id);
    }
}

async function onLikeDislike(e) {
    let el = e.target.parentElement.parentElement;
    if (e.target.classList.contains('fa-heart')) {
        
        if (e.target.classList.contains('far')) {
            await likeSong(el.id);
            e.target.classList.remove('far');
            e.target.classList.add('fas');
        } else {
            await dislikeSong(el.id);
            e.target.classList.remove('fas');
            e.target.classList.add('far');
        }
    }
    
}

export function selectSong(songId) {
    document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
    let song = document.getElementById(songId);
    if (song) song.classList.add('current');
}

function getTimeCodeFromNum(num) {
    let seconds = parseInt(num);
    let minutes = parseInt(seconds / 60);
    seconds -= minutes * 60;
    const hours = parseInt(minutes / 60);
    minutes -= hours * 60;
  
    if (hours === 0) return `${minutes}:${String(seconds % 60).padStart(2, 0)}`;
    return `${String(hours).padStart(2, 0)}:${minutes}:${String(
    seconds % 60).
    padStart(2, 0)}`;
  }
  