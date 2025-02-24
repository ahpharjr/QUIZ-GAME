
function playClickSound() {
    const audioClick = new Audio('/audios/TutorialWalk.mp3');
    audioClick.playbackRate = 3;
    audioClick.play();
}

function playFinalSound() {
    const audioClick = new Audio('/audios/TutorialFinish.mp3');
    audioClick.playbackRate = 1;
    audioClick.play();
}

function getOnboardingSteps() {
    let steps = [
        {
            title: "Welcome to Quiz Game! 🚀",
            intro: "Ready to embark on an exciting journey of knowledge and achievement? Here’s your ultimate guide to getting started."
        }
    ];

    if (window.innerWidth > 764.99) {
        steps.push(
            {
                element: document.querySelector('.info-container'),
                title: "Your Mission 🌟",
                intro: "Become a knowledge explorer! Earn XP points through quizzes and flashcards to level up and reap exciting rewards!"
            },
            {
                element: document.querySelector('.left-div'),
                title: "To Level Up",
                intro: "Earn Experience Points (XP) by completing activities.<br> Once you reach the required XP, you’ll level up and unlock new challenges.",
                position: 'right'
            },
            {
                element: document.querySelector('.right-div'),
                title: "Unlock Achievements 🏆",
                intro: "Push your limits and conquer milestones to earn exclusive achievement badges!"
            }
        );
    } else {
        steps.push(
            {
                element: document.querySelector('.mySwiper'),
                title: "Your Mission 🌟",
                intro: "Become a knowledge explorer! Earn XP points through quizzes and flashcards to level up and reap exciting rewards!"
            },
            // {
            //     element: document.querySelector('.swiper-slide-info'),
            //     title: "To Level Up",
            //     intro: "Earn Experience Points (XP) by completing activities.<br> Once you reach the required XP, you’ll level up and unlock new challenges.",
            //     position: 'right'
            // },
            // {
            //     element: document.querySelector('.swiper-slide-achievement'),
            //     title: "Unlock Achievements 🏆",
            //     intro: "Push your limits and conquer milestones to earn exclusive achievement badges!"
            // }
        );
    }

    steps.push(
        {
            element: document.querySelector('.learning-zone-box'),
            title: "Learning Zone 📘",
            intro: "Expand your knowledge through interactive flashcards—perfect for sharpening your understanding at your own pace."
        },
        {
            element: document.querySelector('.game-zone-box'),
            title: "Gaming Zone 🎮",
            intro: "Put your brainpower to the test with fun, fast-paced quizzes. Climb the leaderboard, improve your score, and show the world your expertise!"
        }
    );

    return steps;
}

function startOnboarding() {
    const intro = introJs();

    intro.setOptions({
        nextLabel: 'Next ',
        prevLabel: 'Back',
        doneLabel: 'Finish',
        skipLabel: 'Skip',
        tooltipClass: 'custom-tooltip',
        highlightClass: 'custom-highlight',
        overlayOpacity: 0.9,
        steps: getOnboardingSteps()
    });

    intro.onbeforechange(function () {
        playClickSound();
    });

    intro.oncomplete(function () {
        playFinalSound();
        localStorage.setItem('onboardingCompleted', 'true');
    });

    intro.onexit(function () {
        localStorage.setItem('onboardingCompleted', 'true');
    });

    intro.start();
}

// Automatically trigger onboarding for new users
window.onload = function () {
    if (!localStorage.getItem('onboardingCompleted')) {
        startOnboarding();
    }
};

