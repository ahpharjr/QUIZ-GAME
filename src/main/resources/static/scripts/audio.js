
var audioClick = new Audio('/audios/click.mp3');
var audioClick1 = new Audio('/audios/click2.mp3');
var backgroundAudio = new Audio('/audios/Rollercoaster - Everet Almond.mp3');
//var backgroundAudio = new Audio('/audios/background.mp3');

    // const audio = new Audio();
    // audio.src = "/audios/click.mp3";
    audioClick.playbackRate = 2;

    function playBackgroundAudioQuiz(){
        // Autoplay when the page loads
        backgroundAudio.loop = true;

        window.addEventListener('load', function () {
            backgroundAudio.play().catch(error => console.log("Autoplay blocked:", error));
        });
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

    //for next button in quiz
    function clickNextAudio(className){
        const nextElement = document.querySelector(className);
        nextElement.addEventListener("click", function(event){
            event.preventDefault();
            audioClick.play();
        })
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
                }, 200);
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
            }, 200);
    
        });
    }

