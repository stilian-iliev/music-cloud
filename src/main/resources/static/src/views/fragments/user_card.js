import { html } from '../../../node_modules/lit-html/lit-html.js';

export const userCardTemplate = (user) => html`
<div class="card mb-4">
            <div class="card-body text-center">
              <img src="${user.imageUrl}" alt="avatar" class="rounded-circle img-fluid" style="width: 150px;">
              <a class="mb-1 text-dark fs-4" href="/user/${user.id}" style="display:block;">${user.username}</a>
              
              
              <div class="d-flex justify-content-center mb-2">
                <button type="button" class="btn btn-primary">Follow</button>
                
              </div>
            </div>
          </div>
`;