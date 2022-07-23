import { html } from '../../../node_modules/lit-html/lit-html.js';
import { followUser, unfollowUser } from '../../api/data.js';

export const userCardTemplate = (user) => html`
<div class="card mb-4">
            <div class="card-body text-center">
              <img src="${user.imageUrl}" alt="avatar" class="rounded-circle img-fluid" style="width: 150px;">
              <a class="mb-1 text-dark fs-4" href="/user/${user.id}" style="display:block;">${user.username}</a>
              
              
              <div class="d-flex justify-content-center mb-2">
                ${user.id != sessionStorage.getItem('userId') ? html`<button @click=${onFollowToggle} type="button" class="btn btn-primary" id="${user.id}">${user.followed ? 'Unfollow' : 'Follow'}</button>` : ''}
                
              </div>
            </div>
          </div>
`;


async function onFollowToggle(e) {
  if (e.target.textContent == 'Follow') {
    await followUser(e.target.id);
    e.target.textContent = 'Unfollow';
  } else if (e.target.textContent == 'Unfollow') {
    await unfollowUser(e.target.id);
    e.target.textContent = 'Follow';
  }

  
}
