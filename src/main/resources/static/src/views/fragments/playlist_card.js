import { html } from '../../../node_modules/lit-html/lit-html.js';

export const playlistCardTemplate = (playlist) => html`
<div class="col-lg-3 col-md-12 mb-4">
          <div class="bg-image hover-zoom ripple shadow-1-strong rounded">
            <img src="${playlist.imageUrl}" class="w-100">
            <a href="/playlist/${playlist.id}">
              <div class="mask" style="background-color: rgba(0, 0, 0, 0.3);">
                <div class="d-flex justify-content-start align-items-start h-100">
                  <h5><span class="badge bg-light pt-2 ms-3 mt-3 text-dark">${playlist.name}</span></h5>
                </div>
              </div>
              <div class="hover-overlay">
                <div class="mask" style="background-color: rgba(253, 253, 253, 0.15);"></div>
              </div>
            </a>
          </div>
        </div>
`;