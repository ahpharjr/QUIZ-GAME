
function flipCard(card) {
    if (card.closest('.swiper-slide').classList.contains('swiper-slide-active')) {
        card.classList.toggle('is-flipped');
    }
}


// Initialize Swiper
function initializeSwiper() {
    return new Swiper(".mySwiper", {
        effect: "coverflow",
        grabCursor: true,
        centeredSlides: true,
        spaceBetween: 50,
        slidesPerView: "auto",
        coverflowEffect: {
            rotate: 0,
            stretch: 0,
            depth: 60,
            modifier: 3.5,
            slideShadows: false,
        },
        pagination: {
            el: ".swiper-pagination",
            type: "fraction",
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        }
    });
}

let swiper = initializeSwiper();


    // Function to ensure the first topic is selected by default
    function selectFirstTopic() {
        console.log('test1');
        // Check if any topic is already selected
        const selectedTopics = document.querySelectorAll('.set-title.selected');
        if (selectedTopics.length === 0) {
            const firstTopic = document.querySelector('.set-title');
            if (firstTopic) {
                firstTopic.classList.add('selected');


                // Trigger the click event to fetch flashcards for the first topic
                firstTopic.click();
            }
        }
    }

    // Add click event listeners to each topic title
    document.querySelectorAll('.set-title').forEach(topic => {
        topic.addEventListener('click', function() {
            const topicId = this.getAttribute('data-topic-id');


            // Highlight the selected topic
            document.querySelectorAll('.set-title').forEach(t => t.classList.remove('selected'));
            this.classList.add('selected');


            // Fetch and update flashcards for the selected topic
            fetch(`/flashcards/${topicId}`)
                .then(response => response.json())
                .then(data => {
                    const flashcardsContainer = document.getElementById('flashcards-container');
                    flashcardsContainer.innerHTML = ''; // Clear existing flashcards

                    data.forEach(card => {
                        flashcardsContainer.innerHTML += `
                            <div class="swiper-slide">
                                <div class="flashcard" onclick="flipCard(this)">
                                    <div class="flashcard-face front">
                                        <div class="content-pic">
                                            <img class="pic" src="/images/4k-mercedes-red-zcj47yx9mq7tfjed.webp">
                                            <div class="star">
                                                <img class="star-icon" src="/icons/icons8-star-50 (2).png" alt="star">
                                                <img class="star-icon-color" src="/icons/icons8-star-50 (1).png" alt="star">
                                                <span class="tooltip">Add to Collection</span>
                                            </div>
                                        </div>
                                        <div class="text-content">
                                            <h3>${card.keyword}</h3>
                                            <p>${card.definition}</p>
                                        </div>
                                    </div>
                                    <div class="flashcard-face back">
                                        <div class="content-pic">
                                            <img class="pic" src="/images/space man.jpg" alt="image">
                                            <div class="star">
                                                <img class="star-icon" src="/icons/icons8-star-50 (2).png" alt="star">
                                                <img class="star-icon-color" src="/icons/icons8-star-50 (1).png" alt="star">
                                                <span class="tooltip">Add to Collection</span>
                                            </div>
                                        </div>
                                        <div class="text-content">
                                            <h3>${card.keyword}</h3>
                                            <p>${card.definition}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>`;
                    });

                    if (swiper) {
                        swiper.destroy(true, true);
                    }
                    swiper = initializeSwiper();
                });

    });
});


// Call the function to ensure the first topic is selected by default
window.onload = selectFirstTopic;
