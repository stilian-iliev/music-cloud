import { html } from '../../node_modules/lit-html/lit-html.js';
import { getPlaylist, getLiked, getMyId } from '../api/data.js';
import {songListTemplate} from './fragments/songlist.js';

export const playlistTemplate = (playlist, liked, me) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
        <div class="col col-lg-9 col-xl-8">
            <div class="card">
                <section>
                    <img src="${playlist.imageUrl}" width="250px" height="250px">
                    <div id="playlist-details"><cite class="title">${playlist.name}</cite>
                        <address><a rel="artist">${playlist.creator.username}</a></address>
                    </div>
                </section>
                ${songListTemplate(playlist.songs, liked.songs, me, playlist.creator.id)}

            </div>
        </div>
    </div>
</section>

`;

let ctx;
export async function playlistPage(ctxT) {
    ctx = ctxT;
    let playlist = await getPlaylist(ctx.params.id)
    let liked = await getLiked();
    let me = await getMyId();
    ctx.render(playlistTemplate(playlist, liked, me));

}