import { html } from '../../node_modules/lit-html/lit-html.js';

const playlistTemplate = (playlist) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
        <div class="col col-lg-9 col-xl-8">
            <div class="card">
                <section>
                    <img src="https://img.discogs.com/dVuWG0JD254HlrRwEAYY4kiQ0Ys=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-7034291-1455617185-2934.jpeg.jpg" width="250px" height="250px">
                    <div id="playlist-details"><cite class="title">Blurryface</cite>
                        <address><a rel="artist">Twenty One Pilots</a></address>
                    </div>
                </section>
                ${songListTemplate(playlist.songs)}

            </div>
        </div>
    </div>
</section>

`;

let ctx;
export async function playlistPage(ctxT) {
    ctx = ctxT;
    
    ctx.render(playlistTemplate());
    

    
    

}