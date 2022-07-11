import { html } from '../../../node_modules/lit-html/lit-html.js';
import {createPlaylist} from '../../api/data.js';


export const createPlaylistTemplate = () => html`
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-mdb-toggle="modal" data-mdb-target="#exampleModal">
  Create new playlist
</button>

<!-- Modal -->
<div class="modal top fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Create Playlist</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
      <form @submit=${async (e) => await onSubmit(e)} action="/playlists/create" method="post">
      <div class="row mb-4 justify-content-center">
          <div class="avatar-upload">
              <div class="avatar-edit">
                  <input type='file' id="imageUpload" accept=".png, .jpg, .jpeg" name="image"/>
                  <label class="circle justify-content-center align-items-center py-1 px-2"  for="imageUpload"> <i class="fas fa-pen"></i></label>
              </div>
              <div class="avatar-preview">
                  <div id="imagePreview"
                  style="background-image: url();"
                  >
                  </div>
              </div>
          </div>
      </div>
      <div class="form-outline mb-4">
                        <input type="text" id="playlistName" name="name" class="form-control">
                        <label class="form-label" for="playlistName" style="margin-left: 0px;">Name</label>
                        <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 66.4px;"></div><div class="form-notch-trailing"></div></div>
                        <div class="invalid-feedback">Name is required.</div></div>
          
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-mdb-dismiss="modal">
          Close
        </button>
        <button type="submit" class="btn btn-primary">Create Playlist</button>
      </div>
    </div>
  </div>
</div>
`;

async function onSubmit(e) {
  e.preventDefault();
  const formData = new FormData(e.target);
  console.log('here');
  if (!formData.get('name').trim()) {
    document.querySelector('#playlistName').classList.add('is-invalid');
    return;
  } else {
    document.querySelector('#playlistName').classList.remove('is-invalid');
  }
  await createPlaylist(formData);
}