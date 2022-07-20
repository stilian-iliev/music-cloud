import { html } from '../../../node_modules/lit-html/lit-html.js';

export const userCardTemplate = (user) => html`
<div class="card mb-4">
            <div class="card-body text-center">
              <img src="${user.imageUrl}" alt="avatar" class="rounded-circle img-fluid" style="width: 150px;">
              <h5 class="my-3">${user.username}</h5>
              
              
              <div class="d-flex justify-content-center mb-2">
                <button type="button" class="btn btn-primary">Follow</button>
                
              </div>
            </div>
          </div>
`;