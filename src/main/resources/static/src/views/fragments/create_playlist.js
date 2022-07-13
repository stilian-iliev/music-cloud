import { html } from '../../../node_modules/lit-html/lit-html.js';
import {createPlaylist} from '../../api/data.js';
import { playlistCardTemplate } from './playlist_card.js';


let ctx;
export const createPlaylistTemplate = (ctxT) => {
  ctx = ctxT;

  return html`
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
      <form @submit=${async (e) => await onSubmit(e)} action="/playlists/create" method="post">
    
      <div class="modal-body">
      
      <div class="row mb-4 justify-content-center">
          <div class="avatar-upload">
              <div class="avatar-edit">
                  <input type='file' id="playlistImage" accept=".png, .jpg, .jpeg" name="image"/>
                  <label class="circle justify-content-center align-items-center py-1 px-2"  for="playlistImage"> <i class="fas fa-pen"></i></label>
              </div>
              <div class="avatar-preview">
                  <div id="playlistImagePreview"
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
          
      
      </div>
      <div class="modal-footer">
        <button id="closeCreatePlaylist" type="button" class="btn btn-secondary" data-mdb-dismiss="modal">
          Close
        </button>
        <button type="submit" class="btn btn-primary">Create Playlist</button>
      </div>
      </form>
    </div>
  </div>
</div>
`;}

//todo fix playlist image preview
function previewPlaylistPic() {
  
  let input = document.getElementById("playlistImagePreview");
  input.addEventListener("change", changePic);

}

function changePic(e) {
  var input = e.target;
  if (input.files && input.files[0]) {
      var file = input.files[0];
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = function(e) {
          document.querySelector("#playlistImage").style.backgroundImage = 'url('+ reader.result +')';
      }
  }
}

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
  let res = await createPlaylist(formData);
  
  ctx.render(playlistCardTemplate(res), document.querySelector("#profilePlaylists"));
  document.querySelector("#closeCreatePlaylist").click();
}