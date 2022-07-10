
import { html } from '../../../node_modules/lit-html/lit-html.js';
export const songListTemplate = (songs) => html`
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
`;

const songPreview = (song) => html`
    <tr class="song" data-track-number="1" data-source="Heavydirtysoul.mp3">
    <td @click=${onClick}><span class="track-number">1</span></td>
    <td width="65%"><cite class="title">${song.title}</cite></td>
    <td width="35%">
        <address><a rel="artist">${song.creator}</a></address>
    </td>
    <td><span class="runtime">${getTimeCodeFromNum(song.duration)}</span></td>
    </tr>
`;

function onClick(e) {
    let el = e.target.parentElement.parentElement;
    if (el.tagName == 'TR') {
        document.querySelectorAll('.current').forEach(e => e.classList.remove('current'));
        el.classList.add('current');
    }
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
  