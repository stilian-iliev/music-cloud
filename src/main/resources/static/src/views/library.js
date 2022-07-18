import { html } from '../../node_modules/lit-html/lit-html.js';
import { getUserPlaylists } from '../api/data.js';
import { playlistCardTemplate } from './fragments/playlist_card.js';

const libraryTemplate = (playlists) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
        <div class="col col-lg-9 col-xl-8 mx-4">
            <div class="card">
            <div class="mx-4">
            <h4 class="my-3"><strong>Your playlists</strong></h4>
            <div class="row row-cols-1 row-cols-md-3 g-4 pb-4 mt-2"> 
            
            
                ${playlists.map(playlistCardTemplate)}
            </div>
            </div>
            </div>
        </div>
    </div>
</section>
`;

export async function libraryPage(ctx) {
    let playlists = await getUserPlaylists(sessionStorage.getItem('userId'));
    ctx.render(libraryTemplate(playlists));
}