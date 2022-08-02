// window.addEventListener("load", () => {
// Possible improvements:
// - Change timeline and volume slider into input sliders, reskinned
// - Change into Vue or React component
// - Be able to grab a custom title instead of "Music Song"
// - Hover over sliders to see preview of timestamp/volume change
import { selectSong } from "./songlist.js";
const audioPlayer = document.querySelector(".audio-player");
const songTitle = document.querySelector(".name");

let queue;
let index;
let audio = new Audio();
let volume = .75;

export function removeFromQueue(index) {
  if (!queue) {
    return;
  }
  queue = queue.splice(index, 1);
}

// export function isPlaying(id) {
//   console.log(id);
//   return queue && index < queue.length && queue[index].id == id;
// }

export function getCurrent() {
  if (!queue) return;
  return queue[index].id;
}

audio.addEventListener(
  "loadeddata",
  () => {
    audioPlayer.querySelector(".time .length").textContent = getTimeCodeFromNum(
    audio.duration);
  
    audio.volume = volume;
  },
  false);

export function queueList(q, offset) {
  queue = q;
  index = offset - 2;
  playNext();
}

function playSong() {
  songTitle.textContent = queue[index].title;
  audio.src=queue[index].songUrl;
  selectSong(queue[index].id);
  
  if (audio.paused) {
    playBtn.click();
  }
}

function playNext() {
    if (index+1 < queue.length) {
      
      index++;
      playSong();
    }

}

function playPrev() {
  if (index > 0) {
    
    
    index--;
    
    playSong();

  }

}


//click on timeline to skip around
const timeline = audioPlayer.querySelector(".timeline");
timeline.addEventListener("click", e => {
  const timelineWidth = window.getComputedStyle(timeline).width;
  const timeToSeek = e.offsetX / parseInt(timelineWidth) * audio.duration;
  audio.currentTime = timeToSeek;
}, false);

//click volume slider to change volume
const volumeSlider = audioPlayer.querySelector(".controls .volume-slider");
volumeSlider.addEventListener('click', e => {
  const sliderWidth = window.getComputedStyle(volumeSlider).width;
  const newVolume = e.offsetX / parseInt(sliderWidth);
  audio.volume = newVolume;
  volume = newVolume;
  audioPlayer.querySelector(".controls .volume-percentage").style.width = newVolume * 100 + '%';
}, false);

//check audio percentage and update time accordingly
setInterval(() => {
  if (audio) {
    const progressBar = audioPlayer.querySelector(".progress");
    progressBar.style.width = audio.currentTime / audio.duration * 100 + "%";
    audioPlayer.querySelector(".time .currentTime").textContent = getTimeCodeFromNum(
    audio.currentTime);
    if (progressBar.style.width == '100%') {

      playNext();
    }
  }

}, 500);

//toggle between playing and pausing on button click
const playBtn = audioPlayer.querySelector(".controls .toggle-play");
playBtn.addEventListener(
"click",
() => {
  if (audio.paused) {
    playBtn.classList.remove("fa-play");
    playBtn.classList.add("fa-pause");
    audio.play();
  } else {
    playBtn.classList.remove("fa-pause");
    playBtn.classList.add("fa-play");
    audio.pause();
  }
},
false);

const nextBtn = document.querySelector(".next");
nextBtn.addEventListener('click', playNext);

const prevBtn = document.querySelector('.previous');
prevBtn.addEventListener('click', playPrev);



audioPlayer.querySelector(".volume-button").addEventListener("click", () => {
  const volumeEl = audioPlayer.querySelector(".volume-container .volume");
  audio.muted = !audio.muted;
  if (audio.muted) {
    volumeEl.classList.remove("icono-volumeMedium");
    volumeEl.classList.add("icono-volumeMute");
  } else {
    volumeEl.classList.add("icono-volumeMedium");
    volumeEl.classList.remove("icono-volumeMute");
  }
});

//turn 128 seconds into 2:08
function getTimeCodeFromNum(num) {
  let seconds = parseInt(num);
  let minutes = parseInt(seconds / 60);
  seconds -= minutes * 60;
  const hours = parseInt(minutes / 60);
  minutes -= hours * 60;

  if (hours === 0) return `${minutes}:${String(seconds % 60).padStart(2, 0)}`;
  return `${String(hours).padStart(2, 0)}:${minutes}:${String(
  seconds % 60).
  padStart(2, 0)}`;
}
// })