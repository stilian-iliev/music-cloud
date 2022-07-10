import { html } from '../../node_modules/lit-html/lit-html.js';
import { getProfileDto, editProfile, getUserSongs } from '../api/data.js';
import { songListTemplate } from './fragments/songlist.js';

const profileTemplate = (user, songs) => html`
<section class="w-100 px-4 py-5 gradient-custom-2" style="border-radius: .5rem .5rem 0 0;">

    <div class="row d-flex justify-content-center">
        <div class="col col-lg-9 col-xl-8">
            <div class="card">
                <div class="rounded-top text-white d-flex flex-row" style="background-color: #000; height:200px;">
                    <div class="ms-4 mt-5 d-flex flex-column" style="width: 150px;">
                        <img src="${user.imageUrl}" alt="Generic placeholder image" class="img-fluid img-thumbnail mt-4 mb-2" style="width: 150px; z-index: 1">
                        <button style="z-index: 1;" type="button" class="btn-dark" data-mdb-toggle="modal" data-mdb-target="#editProfileModal">Edit Profile</button>
                    </div>
                    <div class="ms-3" style="margin-top: 130px;">
                        <h5>${user.fullName}</h5>
                        <p>Role</p>
                    </div>
                </div>
                <div class="p-4 text-black" style="background-color: #f8f9fa;">
                    <div class="d-flex justify-content-end text-center py-1">
                        <div>
                            <p class="mb-1 h5">253</p>
                            <p class="small text-muted mb-0">Songs</p>
                        </div>
                        <div class="px-3">
                            <p class="mb-1 h5">1026</p>
                            <p class="small text-muted mb-0">Followers</p>
                        </div>
                        <div>
                            <p class="mb-1 h5">478</p>
                            <p class="small text-muted mb-0">Following</p>
                        </div>
                    </div>
                </div>
                <div class="mx-4">
                    <!-- Pills navs -->
                    <ul class="nav nav-pills nav-fill mb-3" id="ex1" role="tablist">
                        <li class="nav-item" role="presentation">
                            <a
                                    class="nav-link active text-dark"
                                    id="ex2-tab-1"
                                    data-mdb-toggle="pill"
                                    href="#ex2-pills-1"
                                    role="tab"
                                    aria-controls="ex2-pills-1"
                                    aria-selected="true"
                            >Songs</a
                            >
                        </li>
                        <li class="nav-item" role="presentation">
                            <a
                                    class="nav-link text-dark"
                                    id="ex2-tab-2"
                                    data-mdb-toggle="pill"
                                    href="#ex2-pills-2"
                                    role="tab"
                                    aria-controls="ex2-pills-2"
                                    aria-selected="false"
                            >Playlists</a
                            >
                        </li>
                    </ul>
                    <!-- Pills navs -->

                    <!-- Pills content -->
                    <div class="tab-content" id="ex2-content">
                        <div
                                class="tab-pane fade show active"
                                id="ex2-pills-1"
                                role="tabpanel"
                                aria-labelledby="ex2-tab-1"
                        >
                            ${songListTemplate(songs)}
                        </div>
                        <div
                                class="tab-pane fade"
                                id="ex2-pills-2"
                                role="tabpanel"
                                aria-labelledby="ex2-tab-2"
                        >
                            Tab 2 content
                        </div>
                    </div>
                    <!-- Pills content -->
                </div>
            </div>
        </div>
    </div>
    <div class="modal top fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true" data-mdb-backdrop="true" data-mdb-keyboard="true">
        <div class="modal-dialog modal-lg ">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProfileModalLabel">Edit your Profile</h5>
                    <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">

                    <form @submit=${async (e) => await onEditProfile(e)} method="post" action="/profile/edit" enctype="multipart/form-data">
                    <div class="row mb-4 justify-content-center">
                        <div class="avatar-upload circle">
                            <div class="avatar-edit">
                                <input type='file' id="imageUpload" accept=".png, .jpg, .jpeg" name="image"/>
                                <label class="circle justify-content-center align-items-center py-1 px-2"  for="imageUpload"> <i class="fas fa-pen"></i></label>
                            </div>
                            <div class="avatar-preview circle">
                                <div id="imagePreview" class="circle"
                                style="background-image: url(${user.imageUrl});"
                                >
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 2 column grid layout with text inputs for the first and last names -->
                    <div class="row mb-4">
                        <div class="col">
                            <div class="form-outline">
                                <input type="text" id="form6Example1" class="form-control ${user.firstName ? "active" : ''}" name="firstName" value="${user.firstName}">
                                <label class="form-label" for="form6Example1" style="margin-left: 0px;">First name</label>
                                <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 68.8px;"></div><div class="form-notch-trailing"></div></div></div>
                        </div>
                        <div class="col">
                            <div class="form-outline">
                                <input type="text" id="form6Example2" class="form-control ${user.lastName ? "active" : ''}" name="lastName" value="${user.lastName}">
                                <label class="form-label" for="form6Example2" style="margin-left: 0px;">Last name</label>
                                <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 68px;"></div><div class="form-notch-trailing"></div></div></div>
                        </div>
                    </div>

                    <!-- Text input -->
                    <div class="form-outline mb-4">
                        <input type="text" id="usernameField" class="form-control ${user.username ? "active" : ''}" name="username" value="${user.username}">
                        <label class="form-label" for="usernameField" style="margin-left: 0px;">Username</label>
                        <div class="form-notch"><div class="form-notch-leading" style="width: 9px;"></div><div class="form-notch-middle" style="width: 55.2px;"></div><div class="form-notch-trailing"></div></div>
                        <div class="invalid-feedback">Username is required.</div></div>
                    <div class="modal-footer">
                    <button id="closeProfileModal" type="button" class="btn btn-light" data-mdb-dismiss="modal">
                        Close
                    </button>

                    <button type="submit" class="btn btn-primary text-dark" style="background-color: #ffac44;">Save changes</button>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </div>
</section>
`;

let songs;
let ctx;
export async function profilePage(ctxT) {
    ctx = ctxT;
    const user = await getProfileDto(ctx.params.id);
    songs = await getUserSongs(ctx.params.id);
    await renderPage(user);
    previewPic();
}

async function renderPage(user) {
    document.querySelector("#navName").textContent = user.username;
    document.querySelector("#navPhoto").src = user.imageUrl;
    document.title = `${user.username} - musiCloud`;
    
    ctx.render(profileTemplate(user, songs));
}


async function onEditProfile(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    if (!formData.get('username').trim()) {
        document.querySelector('#usernameField').classList.add('is-invalid');
        return;
    } else {
        document.querySelector('#usernameField').classList.remove('is-invalid');
    }
    let res = await editProfile(formData);
    document.querySelector("#closeProfileModal").click();
    await renderPage(res);
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