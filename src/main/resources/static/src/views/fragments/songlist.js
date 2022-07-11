
import { queueList, isPlaying } from './audioplayer.js'
import { html } from '../../../node_modules/lit-html/lit-html.js';
import {isLiked} from '../../api/data.js';

let songList;
let track;
export const songListTemplate = (songs) => {
    document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
    songList = songs;
    track = 0;
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


                </tbody></table>
            
        </section>
`;}

const songPreview = (song) => {
    track++;
    return html`
    <tr id="${song.id}" class="song ${isPlaying(song.id) ? 'current' : ''}" data-track-number="${track}" data-source="Heavydirtysoul.mp3">
    <td @click=${onClick} class="playTrack"><span class="track-number">${track}</span></td>
    <td width="65%"><cite class="title">${song.title}</cite></td>
    <td width="35%">
        <address><a rel="artist" href="${'/user/'+song.creator.id}">${song.creator.username}</a></address>
    </td>
    <td @click=${onClick}>${isLiked(song.id) ? html`<i class="far fa-heart px-2"></i>` : html`<i class="fas fa-heart px-2"></i>`}<i class="fas fa-ellipsis-v px-2"></i><span class="runtime">${getTimeCodeFromNum(song.duration)}</span></td>
    </tr>
`;}

function onClick(e) {
    let el = e.target.parentElement.parentElement;
    if (el.tagName == 'TR' && e.target.parentElement.classList.contains('playTrack')) {
        queueList(songList, el.getAttribute('data-track-number'));
        selectSong(el.id);
    } else {
        console.log(e.target);
    }
}

export function selectSong(songId) {
    document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
    document.getElementById(songId).classList.add('current');

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
  