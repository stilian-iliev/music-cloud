import { html } from '../../node_modules/lit-html/lit-html.js';
import { getPlaylist, getLiked, getMyId } from '../api/data.js';
import {songListFragment} from './fragments/songlist.js';

export const playlistTemplate = async (playlist, liked, isOwner) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
        <div class="col col-lg-9 col-xl-8">
            <div class="card">
                <section>
                
                    <img src="${playlist.imageUrl}" width="250px" height="250px">
                    <div id="playlist-details"><cite class="title">${playlist.name}</cite>
                        <address><a rel="artist">${playlist.creator.username}</a></address>
                        ${isOwner ? editPlaylistModal : ''}
                    </div>
                </section>
                ${await songListFragment(playlist.songs, liked.songs, playlist.creator.id, ctx)}

            </div>
        </div>
    </div>
</section>

`;

const editPlaylistModal = () => html`
<div class="d-flex align-items-end"> <button class="btn">edit playlist </button></div>
`;

let ctx;
export async function playlistPage(ctxT) {
    ctx = ctxT;
    let playlist = await getPlaylist(ctx.params.id)
    let liked = await getLiked();
    let isOwner = await getMyId() == playlist.creator.id;

    ctx.render(await playlistTemplate(playlist, liked, isOwner));

}