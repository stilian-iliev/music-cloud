import { html } from '../../node_modules/lit-html/lit-html.js';
import { getPlaylist, getLiked, getMyId, editPlaylist, deletePlaylist } from '../api/data.js';
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
                        ${isOwner ? editPlaylistModal() : ''}
                    </div>
                </section>
                ${await songListFragment(playlist.songs, liked.songs, playlist.creator.id, ctx)}

            </div>
        </div>
    </div>
</section>

`;

const editPlaylistModal = () => html`
<div class="d-flex align-items-end"> <button type="button" class="btn" data-mdb-toggle="modal" data-mdb-target="#playlistModal">edit playlist </button></div>

<!-- Modal -->
<div class="modal top fade" id="playlistModal" tabindex="-1" aria-labelledby="editPlaylistModal" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editPlaylistModal">Edit Playlist</h5>
        <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
      </div>
      <form @submit=${onEdit} action="/playlists/edit" method="post">
    
      <div class="modal-body">
      
        <div class="row mb-4 justify-content-center">
            <div class="avatar-upload">
                <div class="avatar-edit">
                    <input type='file' id="imageUpload" accept=".png, .jpg, .jpeg" name="image"/>
                    <label class="circle justify-content-center align-items-center py-1 px-2"  for="imageUpload"> <i class="fas fa-pen"></i></label>
                </div>
                <div class="avatar-preview">
                    <div id="imagePreview" class=""
                    style="background-image: url(${playlist.imageUrl});"
                    >
                    </div>
                </div>
            </div>
        </div>
      <div class="form-outline mb-4">
                        <input type="text" id="playlistName" name="name" value="${playlist.name}" class="form-control active">
                        <label class="form-label" for="playlistName" style="margin-left: 0px;">Name</label>
                        <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 66.4px;"></div><div class="form-notch-trailing"></div></div>
                        <div class="invalid-feedback">Name is required.</div></div>
          
      
      </div>
      <div class="modal-footer">
        <button @click=${onDelete} type="button" class="btn btn-danger">Delete playlist</button>
        <button id="closeEditPlaylist" type="button" style="display: none;" data-mdb-dismiss="modal"/>
        <button type="submit" class="btn btn-primary">Edit Playlist</button>
      </div>
      </form>
    </div>
  </div>
</div>
`;

let ctx;
let playlist;
export async function playlistPage(ctxT) {
    ctx = ctxT;
    playlist = await getPlaylist(ctx.params.id)
    let liked = await getLiked();
    let isOwner = await getMyId() == playlist.creator.id;

    ctx.render(await playlistTemplate(playlist, liked, isOwner));
    if (isOwner) {
        previewPic();
    }

}

async function onEdit(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    if (!formData.get('name').trim()) {
      document.querySelector('#playlistName').classList.add('is-invalid');
      return;
    } else {
      document.querySelector('#playlistName').classList.remove('is-invalid');
    }
    console.log("here");
    await editPlaylist(playlist.id, formData);
    
    ctx.page.redirect(window.location.pathname);
    document.querySelector("#closeEditPlaylist").click();
}

async function onDelete(e) {
  window.confirm("Are you sure you want to delete this playlist?");
  await deletePlaylist(playlist.id);
  document.querySelector("#closeEditPlaylist").click();
  ctx.page.redirect("/");
}

function previewPic() {
    let input = document.getElementById("imageUpload");
    input.addEventListener("change", changePic)
}

function changePic(e) {
    var input = e.target;
    if (input.files && input.files[0]) {
        var file = input.files[0];
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function(e) {
            document.querySelector("#imagePreview").style.backgroundImage = 'url('+ reader.result +')';
        }
    }
}