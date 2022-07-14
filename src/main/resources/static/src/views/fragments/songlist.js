
import { queueList, isPlaying } from './audioplayer.js'
import { html } from '../../../node_modules/lit-html/lit-html.js';
import {likeSong, dislikeSong, getMyId, getUserPlaylists, addSongToPlaylist, removeSongFromPlaylist} from '../../api/data.js';

let userPlaylists;
let songList;
let trackNumber;
let userLikedSongs;
let myId;
let playlistCratorId;
let loc;
export const songListTemplate = (songs) => {
    document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
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
<div class="modal top fade" id="addToPlaylistModal" tabindex="-1" aria-labelledby="addToPlaylistModalLabel" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
<div class="modal-dialog modal-sm ">
    <div class="modal-content">
    <div class="modal-header">
        <h5 class="modal-title" id="addToPlaylistModalLabel">Choose playlist</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
    </div>
    <div class="modal-body">
    <div >
    <!-- Default radio -->
        ${userPlaylists.map(radioTemplate)}
      </div>
      <button id="closeAddToPlaylist" data-mdb-dismiss="modal" style="display: none;"/>
      </div>
    </div>
</div>
</div>
`;}

const radioTemplate = (playlist) => html`
<div class="form-check">
  <input @change=${onAddToPlaylist} id="${playlist.id}" class="form-check-input pl-radio" type="radio" name="flexRadioDefault"/>
  <label class="form-check-label" for="flexRadioDefault1"> ${playlist.name} </label>
</div>
`;

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
    <li @click=${setModalSongId}><a class="dropdown-item" href="#" data-mdb-toggle="modal" data-mdb-target="#addToPlaylistModal">Add to playlist</a></li>
    ${loc == "playlist" && myId == playlistCratorId ? html`<li @click=${onRemoveFromPlaylist}><a class="dropdown-item" href="#">Remove from playlist</a></li>` : ''}
  </ul><span class="runtime">${getTimeCodeFromNum(song.duration)}</span></td>
    </tr>
`;}

let ctx;
export async function songListFragment(songs, liked, pci, ctxT) {
    if (ctxT) {
        ctx = ctxT;
    }
    loc = window.location.pathname.split('/')[1];
    myId = await getMyId();
    playlistCratorId = pci;
    songList = songs;
    userLikedSongs = liked.map(s => s.id);
    trackNumber = 0;
    userPlaylists = await getUserPlaylists(myId);
    return songListTemplate(songs);
}

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

function setModalSongId(e) {
    let songId = e.target.parentElement.parentElement.parentElement.parentElement.id;
    document.querySelector("#addToPlaylistModal").setAttribute('song', songId);
}

async function onAddToPlaylist(e) {
    let songId = document.querySelector('#addToPlaylistModal').getAttribute('song');
    let playlistId = e.target.id;
    await addSongToPlaylist(songId, playlistId);
    document.querySelectorAll('.pl-radio').forEach(r => r.checked = false);
    document.querySelector("#closeAddToPlaylist").click();
}

async function onRemoveFromPlaylist(e) {
    let songId = e.target.parentElement.parentElement.parentElement.parentElement.id;
    let playlistId = ctx.params.id;
    await removeSongFromPlaylist(songId, playlistId);
    ctx.page.redirect(window.location.pathname);
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
  