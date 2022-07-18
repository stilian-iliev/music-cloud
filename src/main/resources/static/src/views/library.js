import { html } from '../../node_modules/lit-html/lit-html.js';
import { getUserPlaylists } from '../api/data.js';
import { playlistCardTemplate } from './fragments/playlist_card.js';

const libraryTemplate = (playlists) => html`

<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
    
        <div class="col col-lg-9 col-xl-8 mx-4">
            <div class="card">
            <!-- Tabs navs -->
<ul class="nav nav-tabs nav-justified mb-3" id="ex1" role="tablist">
  <li class="nav-item" role="presentation">
    <a
      class="nav-link active"
      id="ex3-tab-1"
      data-mdb-toggle="tab"
      href="#ex3-tabs-1"
      role="tab"
      aria-controls="ex3-tabs-1"
      aria-selected="true"
      >Link</a
    >
  </li>
  <li class="nav-item" role="presentation">
    <a
      class="nav-link"
      id="ex3-tab-2"
      data-mdb-toggle="tab"
      href="#ex3-tabs-2"
      role="tab"
      aria-controls="ex3-tabs-2"
      aria-selected="false"
      >Very very very very long link</a
    >
  </li>

</ul>
<!-- Tabs navs -->

<!-- Tabs content -->
<div class="tab-content" id="ex2-content">
  <div
    class="tab-pane fade show active"
    id="ex3-tabs-1"
    role="tabpanel"
    aria-labelledby="ex3-tab-1"
  >
    Tab 1 content
  </div>
  <div
    class="tab-pane fade"
    id="ex3-tabs-2"
    role="tabpanel"
    aria-labelledby="ex3-tab-2"
  >
    Tab 2 content
  </div>

</div>
<!-- Tabs content -->
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