
import { render } from '../node_modules/lit-html/lit-html.js';
import { profilePage } from './views/profile.js';
import { libraryPage } from './views/library.js';

import page from "//unpkg.com/page/page.mjs";

const main = document.querySelector('#page-container');

page('/user/:id', decorateContext, profilePage);
page('/search/:id', decorateContext, profilePage);
page('/', decorateContext, libraryPage);
// page('/playlist/:id', decorateContext, profilePage);


// Start application
page.start();
console.log("page started");

function decorateContext(ctx, next) {
    ctx.render = (content) => render(content, main);
    next();
}