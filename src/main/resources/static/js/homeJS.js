function setMovieData(name, description, img, ratingKp, ratingImdb, year) {
    document.querySelector('.modal-title').textContent = name;
    document.querySelector('.modal-body .film-description').textContent = description;
    document.querySelector('.modal-body .film-img').src = img;
    document.querySelector('.modal-body .rating-imdb').textContent = ratingImdb;
    document.querySelector('.modal-body .rating-kp').textContent = ratingKp;
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
const toggleOptions = document.querySelectorAll('.toggle-option');

toggleOptions.forEach(option => {
    option.addEventListener('click', () => {
        toggleOptions.forEach(opt => opt.classList.remove('active'));
        option.classList.add('active');

        // Здесь можно добавить логику для обработки выбранного варианта
        const selectedOption = option.dataset.option;
        console.log(`Selected option: ${selectedOption}`);
    });
});
