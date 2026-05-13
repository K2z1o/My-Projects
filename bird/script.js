//tło
let board;
let boardwidth = 440;
let boardheight = 720;
let context;

//ptak
let birdwidth = 72;
let birdheight = 51;
let birdx = boardwidth/8;
let birdy = boardheight/2;
let birdimage;

let bird = {
    x : birdx,
    y : birdy,
    width : birdwidth,
    height : birdheight,
}

//rury
let pipearray = [];
let pipewidth = 100;
let pipeheight = 600;
let pipex = boardwidth;
let pipey = 0;

let uppipeimage;
let downpipeimage;

//fizyka
let velocityx = -2;
let velocityy = 0;
let gravity = 0.4;
let gameover = true;
let score = 0;

//dzwiek
let music = new Audio("./music.mp3");
music.volume = 0.2;
music.loop = true;
let jumpaudio = new Audio("./jump.mp3");
jumpaudio.volume = 0.4;
let hitaudio = new Audio("./hit.mp3");
hitaudio.volume = 0.6;

window.onload = function() {
    board = document.getElementById("board");
    board.height = boardheight;
    board.width = boardwidth;
    context = board.getContext("2d"); //rysowanie na tle

    //zdjecia
    birdimage = new Image();
    birdimage.src = "./bird.png";
    birdimage.onload = function() {
        context.drawImage(birdimage, bird.x, bird.y, bird.width, bird.height);
    }

    uppipeimage = new Image();
    uppipeimage.src = "./uppipe.png";

    downpipeimage = new Image();
    downpipeimage.src = "./downpipe.png";

    //tytul
    context.fillStyle = "#603F26";
    context.font = "2em Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif";
    context.fillText("Press Up Arrow or Space to Start", 12, 320);

    requestAnimationFrame(update);
    setInterval(placepipes, 2000);
    document.addEventListener("keydown", movebird);

    document.getElementById("sound1").addEventListener("click", function() {
        if (jumpaudio.volume > 0 || hitaudio.volume > 0) {
            jumpaudio.volume = 0;
            hitaudio.volume = 0;
            this.querySelector("span").textContent = "volume_off";
        } else {
            jumpaudio.volume = 0.4;
            hitaudio.volume = 0.6;
            this.querySelector("span").textContent = "volume_up";
        }
    });

    document.getElementById("sound2").addEventListener("click", function() {
        if (music.volume > 0) {
            music.volume = 0;
            this.querySelector("span").textContent = "music_off";
        } else {
            music.volume = 0.2;
            this.querySelector("span").textContent = "music_note";
        }
    });
}

function update() {
    requestAnimationFrame(update);
    if (gameover) {
        return;
    }
    context.clearRect(0, 0, board.width, board.height);

    //ptak
    velocityy += gravity;
    bird.y = Math.max(bird.y + velocityy, 0);
    context.drawImage(birdimage, bird.x, bird.y, bird.width, bird.height);

    if (bird.y > board.height) {
        gameover = true;
        hitaudio.play();
    }

    //rury
    for (let i = 0; i < pipearray.length; i++){
        let pipe = pipearray[i];
        pipe.x += velocityx;
        context.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height);

        if (!pipe.passed && bird.x > pipe.x + pipe.width) {
            score += 0.5;
            pipe.passed = true;
        }

        if (detectcollision(bird, pipe)) {
            gameover = true;
            hitaudio.play();
        }
    }

    //usuwanie rur
    while (pipearray.length > 0 && pipearray[0].x < -80) {
        pipearray.shift();
    }

    //wynik
    context.fillStyle = "#603F26";
    context.font = "4em Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif";
    context.fillText(score, 8, 64);

    if (gameover) {
        context.fillStyle = "#603F26";
        context.font = "5.2em Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif";
        context.fillText("Game Over", 40, 360)
    }
}

function placepipes() {
    if (gameover) {
        return;
    }

    let randompipey = pipey - pipeheight/4 - Math.random()*(pipeheight/2);
    let openingspace = board.height/4;

    let uppipe = {
        img : uppipeimage,
        x : pipex,
        y : randompipey,
        width : pipewidth,
        height : pipeheight,
        passed : false,
    }

    pipearray.push(uppipe);

    let downpipe = {
        img : downpipeimage,
        x : pipex,
        y : randompipey + pipeheight + openingspace,
        width : pipewidth,
        height : pipeheight,
        passed : false,
    }

    pipearray.push(downpipe);
}

function movebird(e) {
    //skok
    if (e.code == "Space" || e.code == "ArrowUp") {
        music.play();
        velocityy = -6;
        jumpaudio.play();

        //reset
        if (gameover) {
            bird.y = birdy;
            pipearray = [];
            score = 0;
            gameover = false;
        }
    }
}

function detectcollision(a, b) {
    return a.x < b.x + b.width && a.x + a.width > b.x &&
    a.y < b.y + b.height && a.y + a.height > b.y;
}