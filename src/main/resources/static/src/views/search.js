import { html } from '../../node_modules/lit-html/lit-html.js';
import { getAllSongs, getLiked, getUserPlaylists, getUserSongs } from '../api/data.js';
import { playlistCardTemplate } from './fragments/playlist_card.js';
import { songListFragment } from './fragments/songlist.js';
import page from "//unpkg.com/page/page.mjs";

const searchPageTemplete = async (songs, liked, playlists) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
    
        <div class="col col-lg-9 col-xl-8 mx-4">
            <div class="card">
<div class="row">
  <div class="col-3">
    <!-- Tab navs -->
    <div
      class="nav flex-column nav-tabs text-center"
      id="v-tabs-tab"
      role="tablist"
      aria-orientation="vertical"
    >
      <a
        class="nav-link active"
        id="v-tabs-home-tab"
        data-mdb-toggle="tab"
        href="#v-tabs-home"
        role="tab"
        aria-controls="v-tabs-home"
        aria-selected="true"
        >Songs</a
      >
      <a
        class="nav-link"
        id="v-tabs-profile-tab"
        data-mdb-toggle="tab"
        href="#v-tabs-profile"
        role="tab"
        aria-controls="v-tabs-profile"
        aria-selected="false"
        >Playlists</a
      >
      <a
        class="nav-link"
        id="v-tabs-messages-tab"
        data-mdb-toggle="tab"
        href="#v-tabs-messages"
        role="tab"
        aria-controls="v-tabs-messages"
        aria-selected="false"
        >Users</a
      >
    </div>
    <!-- Tab navs -->
  </div>

  <div class="col-9">
    <!-- Tab content -->
    <div class="tab-content" id="v-tabs-tabContent">
      <div
        class="tab-pane fade show active"
        id="v-tabs-home"
        role="tabpanel"
        aria-labelledby="v-tabs-home-tab"
      >
        ${await songListFragment(songs, liked)}
      </div>
      <div
        class="tab-pane fade"
        id="v-tabs-profile"
        role="tabpanel"
        aria-labelledby="v-tabs-profile-tab"
      >
        <div class="row row-cols-1 row-cols-md-3 g-4 pb-4">
                            
        ${playlists.map(playlistCardTemplate)}
        </div>
      </div>
      <div
        class="tab-pane fade"
        id="v-tabs-messages"
        role="tabpanel"
        aria-labelledby="v-tabs-messages-tab"
      >
        Messages content
      </div>
    </div>
    <!-- Tab content -->
  </div>
</div>
</div>
</div>
</div>
</section>
`;

document.querySelector('#search').addEventListener('submit', onSearch);

let ctx;
export async function searchPage(ctxT) {
  ctx = ctxT;
  let songs = await getAllSongs();
  let liked = await getLiked();
  //todo
  let playlists = await getUserPlaylists(sessionStorage.getItem('userId'));
  console.log(playlists);
    ctx.render(await searchPageTemplete(songs, liked.songs, playlists));
}

async function onSearch(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  page.redirect('/search?q=' + formData.get('q'));

  console.log("searching");
}