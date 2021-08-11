
create schema if not exists mancala;
CREATE TABLE mancala.GAME
(
    gameId       LONG AUTO_INCREMENT PRIMARY KEY,
    playerAmount INT(1) NOT NULL
);

CREATE TABLE mancala.PIT
(

    pitId LONG AUTO_INCREMENT PRIMARY KEY,
    position INT(1) NOT NULL,
    stones INT(2) NOT NULL,
    bigPit boolean NOT NULL,
    player varchar,
    gameId LONG ,
    foreign key (gameId) references GAME(gameId)
);
/*
CREATE TABLE PLAYER
(
    playerId LONG AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);*/

