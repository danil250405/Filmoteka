function setMovieData(name, description, img, ratingKp, ratingImdb, year) {
    document.querySelector('.modal-title').textContent = name;
    document.querySelector('.modal-body .film-description').textContent = description;
    document.querySelector('.modal-body .film-img').src = img;
    document.querySelector('.modal-body .rating-imdb').textContent = ratingImdb;
    document.querySelector('.modal-body .rating-kp').textContent = ratingKp;
    document.querySelector('.modal-body .year').textContent = year;

}