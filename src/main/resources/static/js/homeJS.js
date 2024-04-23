function setMovieData(name, description, img, ratingKp, ratingImdb, year) {
    document.querySelector('.modal-title').textContent = name;
    document.querySelector('.modal-body .film-description').textContent = description;
    document.querySelector('.modal-body .film-img').src = img;
    const ratingImdbElement = document.querySelector('.modal-body .rating-imdb');
    ratingImdbElement.classList.add('rating-imdb');
    const ratingImdbPercent = (ratingImdb / 10) * 100;
    ratingImdbElement.style.setProperty('--percent', `${ratingImdbPercent}%`);
    const valueImdb = document.getElementById("imdb-value-rating");
    valueImdb.innerHTML = ratingImdb;

    const ratingKpElement = document.querySelector('.modal-body .rating-kp');
    ratingKpElement.classList.add('rating-kp');
    const ratingKpPercent = (ratingKp / 10) * 100;
    ratingKpElement.style.setProperty('--percent', `${ratingKpPercent}%`);
    const valueKp = document.getElementById("imdb-kp-rating");
    valueKp.innerHTML = ratingKp;
    document.querySelector('.modal-body .year').textContent = year;

}

//
// const categoriesContainer = document.querySelector('.categories-container');
// const prevBtn = document.querySelector('.prev-btn');
// const nextBtn = document.querySelector('.next-btn');
// const progressIndicator = document.querySelector('.progress-indicator');
// const progressItems = document.querySelectorAll('.progress-item');
//
// let currentIndex = 0;
// const scrollAmount = categoriesContainer.offsetWidth;
//
// prevBtn.addEventListener('click', () => {
//     currentIndex = (currentIndex === 0) ? progressItems.length - 1 : currentIndex - 1;
//     updateProgressIndicator();
//     categoriesContainer.scrollLeft -= scrollAmount;
// });
//
// nextBtn.addEventListener('click', () => {
//     currentIndex = (currentIndex === progressItems.length - 1) ? 0 : currentIndex + 1;
//     updateProgressIndicator();
//     categoriesContainer.scrollLeft += scrollAmount;
// });
//
// function updateProgressIndicator() {
//     progressItems.forEach((item, index) => {
//         if (index === currentIndex) {
//             item.classList.add('active');
//         } else {
//             item.classList.remove('active');
//         }
//     });
// }
//
//

const faqItems = document.querySelectorAll('.faq-item');
//plus/minus
faqItems.forEach((item) => {
    const toggle = item.querySelector('.faq-toggle');
    const answer = item.querySelector('.faq-answer');

    toggle.addEventListener('click', () => {
        item.classList.toggle('active');

        if (item.classList.contains('active')) {
            answer.style.maxHeight = answer.scrollHeight + 'px';
            toggle.textContent = '−'; // Устанавливаем минус
        } else {
            answer.style.maxHeight = null;
            toggle.textContent = '+'; // Возвращаем плюс
        }
    });
});

// month_year
const toggleOptions = document.querySelectorAll('.toggle-option');
const planItems = document.querySelectorAll('.plan-item');

toggleOptions.forEach(option => {
    option.addEventListener('click', () => {
        toggleOptions.forEach(opt => opt.classList.remove('active'));
        option.classList.add('active');

        const selectedOption = option.dataset.option;

        planItems.forEach(item => {
            const costSpan = item.querySelector('.cost');
            const periodSpan = item.querySelector('.period');

            if (selectedOption === 'yearly') {
                costSpan.textContent = `$${costSpan.dataset.yearly}`;
                periodSpan.textContent = '/year';
            } else {
                costSpan.textContent = `$${costSpan.dataset.monthly}`;
                periodSpan.textContent = '/month';
            }
        });
    });
});
// select



// genres
const categoriesContainer = document.querySelector('.categories-list-container');
const categoriesList = categoriesContainer.querySelector('.categories-card-list');
const prevBtn = document.querySelector('.prev-btn');
const nextBtn = document.querySelector('.next-btn');

let currentIndex = 0;
let itemsPerPage = 5; // Количество жанров, отображаемых на одной странице

// Функция для обновления количества отображаемых элементов в зависимости от ширины окна
function updateItemsPerPage() {
    const containerWidth = categoriesContainer.offsetWidth;
    if (containerWidth < 1100) {
        itemsPerPage = 6;
    }else if(containerWidth < 1300 && containerWidth > 1200){
        itemsPerPage = 4;
    }
    else if (containerWidth > 1400){
        itemsPerPage = 5;
    }

}

// Функция для отображения жанров на странице
function showCategories(startIndex, endIndex) {
    const categoryItems = categoriesList.querySelectorAll('.categories-card');
    const itemWidth = categoryItems[0].offsetWidth + categoryItems[0].offsetMargin;
    const translateX = -(startIndex * itemWidth);

    categoriesList.style.transform = `translateX(${translateX}px)`;

    categoryItems.forEach((item, index) => {
        if (index >= startIndex && index < endIndex) {
            item.style.display = 'block';
            item.style.animation = 'fadeIn 0.5s ease forwards';
        } else {
            item.style.display = 'none';
            item.style.animation = 'none';
        }
    });
}

// Обновите itemsPerPage при загрузке страницы
updateItemsPerPage();

// Обновляйте itemsPerPage при изменении размера окна
window.addEventListener('resize', updateItemsPerPage);

// Вызовите showCategories с обновленным itemsPerPage
showCategories(currentIndex, currentIndex + itemsPerPage);
// Функция для обновления прогресс-индикатора
function updateProgressIndicator(currentPage, totalPages) {
    const progressIndicator = document.querySelector('.progress-indicator');
    progressIndicator.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const progressItem = document.createElement('span');
        progressItem.classList.add('progress-item');
        if (i === currentPage - 1) {
            progressItem.classList.add('active');
        }
        progressItem.innerHTML = '<svg viewBox="0 0 512 512" xml:space="preserve" xmlns="http://www.w3.org/2000/svg" enable-background="new 0 0 512 512"><path d="M417.4 224H94.6C77.7 224 64 238.3 64 256s13.7 32 30.6 32h322.8c16.9 0 30.6-14.3 30.6-32s-13.7-32-30.6-32z" fill="#333333" class="fill-000000"></path></svg>';
        progressIndicator.appendChild(progressItem);
    }
}

// Функция для обработки клика на кнопку "Prev"
function handlePrevClick() {
    if (currentIndex > 0) {
        currentIndex--;
        const startIndex = currentIndex * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        showCategories(startIndex, endIndex);
        updateProgressIndicator(currentIndex + 1, Math.ceil(categoriesList.children.length / itemsPerPage));
    }
}

// Функция для обработки клика на кнопку "Next"
function handleNextClick() {
    if (currentIndex < Math.ceil(categoriesList.children.length / itemsPerPage) - 1) {
        currentIndex++;
        const startIndex = currentIndex * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        showCategories(startIndex, endIndex);
        updateProgressIndicator(currentIndex + 1, Math.ceil(categoriesList.children.length / itemsPerPage));
    }
}

// Обработчики событий для кнопок навигации
prevBtn.addEventListener('click', handlePrevClick);
nextBtn.addEventListener('click', handleNextClick);

// Первоначальная загрузка жанров
showCategories(0, itemsPerPage);
updateProgressIndicator(1, Math.ceil(categoriesList.children.length / itemsPerPage));

