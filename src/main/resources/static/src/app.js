
import { render } from '../node_modules/lit-html/lit-html.js';
import { profilePage } from './views/profile.js';
import { libraryPage } from './views/library.js';
import { playlistPage } from './views/playlist.js';

import page from "//unpkg.com/page/page.mjs";


const main = document.querySelector('#page-container');

page('/user/:id', decorateContext, profilePage);
page('/search', decorateContext, playlistPage);
page('/', decorateContext, libraryPage);
// page('/playlist/:id', decorateContext, profilePage);
export const csrf = (document.querySelector('[name = _csrf]'));
console.log(csrf);


// Start application
page.start();
console.log("page started");

function decorateContext(ctx, next) {
    ctx.render = (content) => render(content, main);
    next();
}