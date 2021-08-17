create schema if not exists mancala;

CREATE TABLE if not exists mancala.GAME
(
    gameId        LONG AUTO_INCREMENT PRIMARY KEY,
    playerAmount  INT(1) NOT NULL,
    playerWhoMove varchar(2),
    finished boolean
);


CREATE TABLE if not exists mancala.PIT
(

    pitId    LONG AUTO_INCREMENT PRIMARY KEY,
    position INT(1)  NOT NULL,
    stones   INT(2)  NOT NULL,
    bigPit   boolean NOT NULL,
    player   varchar(2),
    gameId   LONG,
    foreign key (gameId) references GAME (gameId)
);

