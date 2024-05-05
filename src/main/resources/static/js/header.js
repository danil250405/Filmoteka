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
document.addEventListener('DOMContentLoaded', function() {
    const searchIcon = document.querySelector('.search-icon');
    const searchInput = document.querySelector('#movie-name');
    const searchResults = document.querySelector('#search-results');
    const searchContainer = document.querySelector('.search-results-container');

    function displaySearchResults(response) {
        // Здесь вы должны реализовать функцию для отображения результатов поиска
        // на основе полученного ответа от сервера
        console.log(response);
    }

    searchIcon.addEventListener('click', () => {
        searchInput.focus();
        if (searchInput.value.trim() !== '') {
            console.log("first")
            $.ajax({
                url: '/find-by-name-filter-page',
                type: 'GET',
                data: { movieName: searchInput.value.trim() },
                success: function(response) {
                    // window.location.href = '/movies?movieName=' + encodeURIComponent(searchInput.value.trim());
                    console.log("first2")
                },
                error: function(xhr, status, error) {
                    console.log('Ошибка AJAX: ' + error);
                }
            });
        }
    });

    searchInput.addEventListener('input', () => {
        if (searchInput.value.trim() !== '') {
            // Здесь никакого дополнительного кода
        }
    });

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

    document.addEventListener('click', hideSearchResults);

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

    function displayMovies(movies) {
        let html = '';
        for (const movie of movies) {
            const movieUrl = `/movieId=${movie.id}`;
            if (`${movie.previewUrl}` !== null && `${movie.previewUrl}` !== 'null') {
                html += `<li>
                  <a href="${movieUrl}"> 
                    <img src="${movie.previewUrl}" alt="">
                    <span> ${movie.name}</span>
                  </a>
                </li>`;
            } else {
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
});