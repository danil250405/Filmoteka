
const revealButton = document.getElementById('revealButton');
const passwordInput = document.getElementById('password');
const eyeSvg = revealButton.querySelector('.eye');
const upperLid = eyeSvg.querySelector('.lid--upper');
const lowerLid = eyeSvg.querySelector('.lid--lower');

let revealed = false;

const toggleReveal = () => {
    revealed = !revealed;
    revealButton.setAttribute('aria-pressed', revealed);
    passwordInput.type = revealed ? 'text' : 'password';

    if (revealed) {
        upperLid.style.animationPlayState = 'paused';
        lowerLid.style.animationPlayState = 'paused';
    } else {
        upperLid.style.animationPlayState = 'running';
        lowerLid.style.animationPlayState = 'running';
    }
};

revealButton.addEventListener('click', toggleReveal);