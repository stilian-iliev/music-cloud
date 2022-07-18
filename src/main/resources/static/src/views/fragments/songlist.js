
import { queueList, isPlaying } from './audioplayer.js'
import { html } from '../../../node_modules/lit-html/lit-html.js';
import {likeSong, dislikeSong, getUserPlaylists, addSongToPlaylist, removeSongFromPlaylist, editSong, deleteSong} from '../../api/data.js';

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
${editSongModal()}
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
    ${myId == song.creator.id ? html`<li @click=${(e) => {setModalSongId(e, "#editSongModal"); document.querySelector("#songTitle").value = songList.find(s => s.id == document.querySelector("#editSongModal").getAttribute('song')).title}}><a class="dropdown-item" type="button" data-mdb-toggle="modal" data-mdb-target="#editSongModal">Edit song</a></li>` : ""}
    <li @click=${(e) => setModalSongId(e, "#addToPlaylistModal")}><a class="dropdown-item" href="#" data-mdb-toggle="modal" data-mdb-target="#addToPlaylistModal">Add to playlist</a></li>
    ${loc == "playlist" && myId == playlistCratorId ? html`<li @click=${onRemoveFromPlaylist}><a class="dropdown-item" href="#">Remove from playlist</a></li>` : ''}
  </ul><span class="runtime">${getTimeCodeFromNum(song.duration)}</span></td>
    </tr>
`;}

const editSongModal = () => html`
<div class="modal top fade" id="editSongModal" tabindex="-1" aria-labelledby="editSongModalLabel" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
<div class="modal-dialog  ">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Edit song</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      <form @submit=${onEditSong}>
          <div class="form-outline mb-4">
            <input type="text" id="songTitle" name="title" class="form-control active">
            <label class="form-label" for="songTitle" style="margin-left: 0px;">Title</label>
            <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 40px;"></div><div class="form-notch-trailing"></div></div>
            <div class="invalid-feedback mb-2">Title is required!</div></div>

          <div class="modal-footer">
            <button @click=${onDeleteSong} type="button" class="btn btn-danger">
              Delete song
            </button>
            <button type="submit" class="btn btn-primary text-dark" style="background-color: #ffac44;">Change title</button>
          </div>
        </form>
        <button id="closeEditSong" type="button" style="display:none;" data-mdb-dismiss="modal"/>
      </div>
    </div>
  </div>
</div>
`;

let ctx;
export async function songListFragment(songs, liked, pci, ctxT) {
    if (ctxT) {
        ctx = ctxT;
    }
    loc = window.location.pathname.split('/')[1];
    myId = sessionStorage.getItem('userId');
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

function setModalSongId(e, domId) {
    let songId = e.target.parentElement.parentElement.parentElement.parentElement.id;
    document.querySelector(domId).setAttribute('song', songId);
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

async function onEditSong(e) {
    e.preventDefault();
    let songId = document.querySelector('#editSongModal').getAttribute('song');

    let formData = new FormData(e.target);
    if (!formData.get('title').trim()) {
        document.querySelector("#editSongModal #songTitle").classList.add("is-invalid");
    } else {
        document.querySelector("#editSongModal #songTitle").classList.remove("is-invalid");
    }
    await editSong(songId, formData);
    document.querySelector("#closeEditSong").click();
    ctx.page.redirect(window.location.pathname);
}

async function onDeleteSong(e) {
    if (!window.confirm(`Are you sure you want to delete this song?`)){ return; }
    let songId = document.querySelector('#editSongModal').getAttribute('song');
    await deleteSong(songId);
    document.querySelector("#closeEditSong").click();
    ctx.page.redirect(window.location.pathname);
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
  