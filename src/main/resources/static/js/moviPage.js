
//for actors
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const castList = document.querySelector('.cast-list');
const castItems = castList.getElementsByClassName('cast-item');

let currentPage = 0;
const itemsPerPage = 6;

function showPage(page) {
    currentPage = page;
    for (let i = 0; i < castItems.length; i++) {
        castItems[i].style.display = 'none';
    }
    for (let i = page * itemsPerPage; i < (page + 1) * itemsPerPage && i < castItems.length; i++) {
        castItems[i].style.display = 'block';
    }
}

prevBtn.addEventListener('click', () => {
    if (currentPage > 0) {
        showPage(currentPage - 1);
    }
});

nextBtn.addEventListener('click', () => {
    if (currentPage < Math.floor((castItems.length - 1) / itemsPerPage)) {
        showPage(currentPage + 1);
    }
});

showPage(0);

//Reviews
// Получаем элементы модального окна и кнопку открытия
var modal = document.getElementById("myModal");
var addReviewButtons = document.getElementsByClassName("addReviewButton");
var closeButton = document.getElementsByClassName("close")[0];

// Перебираем все кнопки и назначаем обработчик события на каждую
Array.from(addReviewButtons).forEach(function(button) {
    button.onclick = function() {
        modal.style.display = "block";
    }
});

// Когда пользователь кликает на кнопку закрытия, закрываем модальное окно
closeButton.onclick = function() {
    modal.style.display = "none";
}

// Когда пользователь кликает вне модального окна, закрываем его
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}


// reviews stars
const ratings = document.querySelectorAll('.rating-wrap');
if (ratings.length > 0){
    initRatings();
}

//base func
function initRatings(){
    let ratingActive, ratingValue;
    for (let index = 0; index < ratings.length; index++){
        const rating = ratings[index];
        initRating(rating);
    }
}
//individual specific rating
function initRating(rating){
    initRatingVars(rating);
    setRatingActiveWidth();
}
//initialization variables
function initRatingVars(rating){
    ratingActive = rating.querySelector('.rating__active');
    ratingValue = rating.querySelector('.rating__value');
}
function setRatingActiveWidth(index = ratingValue.innerHTML) {
    const ratingActiveWidth = index / 0.10;
    ratingActive.style.width = `${ratingActiveWidth}%`;
}

//reviews pagination
const prevBtnRev = document.querySelector('.prevBtn-reviews');
const nextBtnRev = document.querySelector('.nextBtn-reviews');
const reviewList = document.querySelector('.review-list');
const reviewItem = reviewList.querySelectorAll('.review-item');

let currentPageRev = 0;
const itemsPerPageRev = 2;

function showPageRev(page) {
    currentPageRev = page;
    for (let i = 0; i < reviewItem.length; i++) {
        reviewItem[i].style.display = 'none';
    }
    for (let i = page * itemsPerPageRev; i < (page + 1) * itemsPerPageRev && i < reviewItem.length; i++) {
        reviewItem[i].style.display = 'block';
    }
}

prevBtnRev.addEventListener('click', () => {
    if (currentPageRev > 0) {
        showPageRev(currentPageRev - 1);
    }
});

nextBtnRev.addEventListener('click', () => {
    if (currentPageRev < Math.floor((reviewItem.length - 1) / itemsPerPageRev)) {
        showPageRev(currentPageRev + 1);
    }
});

showPageRev(0);
