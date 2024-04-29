const  hamburger = document.querySelector(".hamburger")
const  navMenu = document.querySelector(".nav-menu")

hamburger.addEventListener("click", () =>{
    hamburger.classList.toggle("active");
    navMenu.classList.toggle("active");
})

document.querySelectorAll(".nav-link").forEach(n => n.addEventListener("click", () =>{
    hamburger.classList.remove("active");
    navMenu.classList.remove("active");
    }

))

// document.addEventListener("touchstart", function(){}, true);
const searchIcon = document.querySelector('.search-icon');
const searchInput = document.querySelector('#movie-name');


searchIcon.addEventListener('click', () => {
    searchInput.focus();
});

searchInput.addEventListener('input', () => {
    if (searchInput.value.trim() !== '') {

        searchIcon.addEventListener('click', () => {

        });
    }
});



// const searchInput = document.querySelector('#movie-name');
const searchResults = document.querySelector('#search-results');
let searchTimer;

searchInput.addEventListener('input', () => {
    clearTimeout(searchTimer);

    if (searchInput.value.length >= 3) {
        searchTimer = setTimeout(() => {
            fetchMovies(searchInput.value);
        }, 3000);
    } else {
        searchResults.innerHTML = '';
    }
});

function fetchMovies(query) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/find-movie?movieName=' + encodeURIComponent(query), true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            const movies = JSON.parse(xhr.responseText);
            displayMovies(movies);
        }
    };
    xhr.send();
}
const searchContainer = document.querySelector('.search-results-container')
function displayMovies(movies) {
    let html = '';
    for (const movie of movies) {
        const movieUrl = `/movieId=${movie.id}`;
        html += `<li>
                    <a href="${movieUrl}"> 
                    <img src="${movie.previewUrl}" alt="">
                       <span> ${movie.name}</span>
                       </a>
                 </li>`;
    }
    searchResults.innerHTML = html;
    searchContainer.style.display = 'block';
}

