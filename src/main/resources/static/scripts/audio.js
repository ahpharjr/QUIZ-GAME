
var audioClick = new Audio('/audios/click.mp3');
var audioClick1 = new Audio('/audios/click2.mp3');
//var backgroundAudio = new Audio('/audios/Rollercoaster - Everet Almond.mp3');
//var backgroundAudio = new Audio('/audios/background.mp3');
var timeRunningOut = new Audio('/audios/Timer3.mp3');
var backgroundAudio = new Audio('/audios/BgSound2.mp3');
var quizFinishAudio = new Audio('/audios/QuizResult.mp3');
var TimeOutNext = new Audio('/audios/TimeOutNext.mp3');

    // const audio = new Audio();
    // audio.src = "/audios/click.mp3";
    audioClick.playbackRate = 3;

    function timerAudio(){
        //audioClick1.play();
        timeRunningOut.playbackRate = 1.5;
        timeRunningOut.play();
    }

    function finishQuizAudio(){
        quizFinishAudio.play();
    }

    function playBackgroundAudioQuiz(){
        // Autoplay when the page loads
        backgroundAudio.loop = true;
        backgroundAudio.play();

        // window.addEventListener('load', function () {
        //     backgroundAudio.play().catch(error => console.log("Autoplay blocked:", error));
        // });
    }

    //for selecting answer
    function clickAnswerAudio(className){
        document.querySelectorAll(className).forEach(ans => {
            ans.addEventListener("click", function(event){
                event.preventDefault();
                audioClick1.playbackRate = 2;
                audioClick1.play();
            })
        })
    }

    let isTimeoutTriggered = false; // Flag to track timeout event

    // Next button click sound
    function clickNextAudio(className) {
        const nextElement = document.querySelector(className);
        nextElement.addEventListener("click", function (event) {
            event.preventDefault();
            if (!isTimeoutTriggered) {
                audioClick.play(); // Play only if not timeout
            }
        });
    }

    // Time-out function for next button
    function clickTimeOut(className) {
        isTimeoutTriggered = true; // Set flag to true on timeout
        const nextElement = document.querySelector(className);
        nextElement.click(); // Simulate click
        TimeOutNext.play(); // Play timeout audio
        
        setTimeout(() => {
            isTimeoutTriggered = false; // Reset flag after timeout action
        }, 500); // Small delay to prevent conflicts
    }

    //audio for each phase and navigate to specific page 
    function handleClickAudio(className , url, dataId ){
        document.querySelectorAll(className).forEach(item => {
            item.addEventListener("click", function(event){
                event.preventDefault();
                audioClick.play();
    
                let targetId = this.getAttribute(dataId); //Get the phase ID from the div
                let targetUrl = `/${targetId}/${url}`; //construct the URL
    
                setTimeout(() => {
                    window.location.href = targetUrl; 
                }, 130);
            });
        });
    }

    //play audio and navigate to other page
    function playNavAudio(idName, url){
        const playLink = document.getElementById(idName);
        playLink.addEventListener("click",function(event){
            event.preventDefault();
            audioClick.play();
            
            document.body.classList.add("fade-out");
            setTimeout(() => {
                window.location.href = `/${url}`;
            }, 130);
    
        });
    }

