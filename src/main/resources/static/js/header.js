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


// Функция для скрытия выпадающего окна
function hideSearchResults(e) {
    if (!searchContainer.contains(e.target) && e.target !== searchInput) {
        searchContainer.style.display = 'none';
    }
}
searchInput.addEventListener('focus', () => {
    if (searchInput.value.trim() !== '') {
        fetchMovies(searchInput.value);
    }
});
// Слушатель событий для скрытия выпадающего окна при клике вне его
document.addEventListener('click', hideSearchResults);

// const searchInput = document.querySelector('#movie-name');
const searchResults = document.querySelector('#search-results');
let searchTimer;

searchInput.addEventListener('input', () => {
    clearTimeout(searchTimer);

    if (searchInput.value.length >= 2) {
        searchTimer = setTimeout(() => {
            fetchMovies(searchInput.value);
        }, 1500);
    } else {
        searchResults.innerHTML = '';
        searchContainer.style.display = 'none';

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
        if (`${movie.previewUrl}` !== null && `${movie.previewUrl}` !== 'null'){
        html += `<li>
                    <a href="${movieUrl}"> 
                    <img src="${movie.previewUrl}" alt="">
                       <span> ${movie.name}</span>
                       </a>
                 </li>`;
        }
        else {
            html += `<li>
                    <a href="${movieUrl}"> 
                    <img src="/static/img/moviefoto.png" alt="">
                       <span> ${movie.name}</span>
                       </a>
                 </li>`;
        }
    }
    searchResults.innerHTML = html;
    searchContainer.style.display = 'block';
    searchContainer.style.zIndex = "2";
}

