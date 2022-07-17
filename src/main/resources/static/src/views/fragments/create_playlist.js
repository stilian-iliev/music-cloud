import { html } from '../../../node_modules/lit-html/lit-html.js';
import {createPlaylist} from '../../api/data.js';
import { playlistCardTemplate } from './playlist_card.js';


let ctx;
export const createPlaylistTemplate = async (ctxT) => {
  ctx = ctxT;

  return html`
<!-- Button trigger modal -->
<button type="button" class="btn btn-dark" data-mdb-toggle="modal" data-mdb-target="#playlistModal">
  Create new playlist
</button>

<!-- Modal -->
<div class="modal top fade" id="playlistModal" tabindex="-1" aria-labelledby="createPlaylistModal" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="createPlaylistModal">Create Playlist</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
      </div>
      <form @submit=${async (e) => await onSubmit(e)} action="/playlists/create" method="post">
    
      <div class="modal-body">
      
      <div class="row mb-4 justify-content-center">
      </div>
      <div class="form-outline mb-4">
                        <input type="text" id="playlistName" name="name" class="form-control">
                        <label class="form-label" for="playlistName" style="margin-left: 0px;">Name</label>
                        <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 66.4px;"></div><div class="form-notch-trailing"></div></div>
                        <div class="invalid-feedback">Name is required.</div></div>
          
      
      </div>
      <div class="modal-footer">
        <button id="closeCreatePlaylist" type="button" class="btn btn-light" data-mdb-dismiss="modal">
          Close
        </button>
        <button type="submit" class="btn sitecolor text-dark">Create Playlist</button>
      </div>
      </form>
    </div>
  </div>
</div>
`;}

async function onSubmit(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  if (!formData.get('name').trim()) {
    document.querySelector('#playlistName').classList.add('is-invalid');
    return;
  } else {
    document.querySelector('#playlistName').classList.remove('is-invalid');
  }
  //todo order playlists on /api/user/playlists
  //todo: close modal add playlist to template
  await createPlaylist(formData);
  document.querySelector("#closeCreatePlaylist").click();
  ctx.page.redirect(window.location.pathname);
}