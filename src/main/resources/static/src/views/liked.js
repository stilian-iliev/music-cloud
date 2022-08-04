import { getLiked } from '../api/data.js';
import {playlistTemplate} from './playlist.js';


let ctx;
export async function likedPlaylistPage(ctxT) {
    ctx = ctxT;
    let liked = await getLiked();
    document.title = `Liked songs - musiCloud`;
    ctx.render(await playlistTemplate(liked, liked, true));

}