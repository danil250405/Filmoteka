
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
var addReviewButton = document.getElementById("addReviewButton");
var closeButton = document.getElementsByClassName("close")[0];

// Когда пользователь кликает на кнопку, открываем модальное окно
addReviewButton.onclick = function() {
    modal.style.display = "block";
}

// Когда пользователь кликает на кнопку закрытия, закрываем модальное окно
closeButton.onclick = function() {
    modal.style.display = "none";
}

// Когда пользователь кликает вне модального окна, закрываем его
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
