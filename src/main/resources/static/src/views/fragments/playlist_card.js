import { html } from '../../../node_modules/lit-html/lit-html.js';

export const playlistCardTemplate = (playlist) => html`
<div class="card" style="max-width: 15rem;">

    <!-- Card image -->
    <div class="view overlay">
        <img width="200" class="card-img-top" src="${playlist.imageUrl}" alt="Card image cap">
    </div>
    <!-- Card content -->
    <div class="card-body elegant-color white-text rounded-bottom">

        <!-- Title -->
        <h4 class="card-title">${playlist.name}</h4>

    </div>

</div>
`;