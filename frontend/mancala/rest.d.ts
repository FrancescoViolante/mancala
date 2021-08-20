/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.27.744 on 2021-08-19 22:45:08.

interface GameDto {
    gameId: number;
    playerAmount: number;
    playerWhoMove: PlayerEnum;
    pitP1: PitDto[];
    pitP2: PitDto[];
    singlePlayer: boolean;
    finished: boolean;
}

interface MovePitRequestModel {
    gameId: number;
    positionClicked: number;
    playerWhoMoved: PlayerEnum;
}

interface NewGameRequestModel {
    playerAmount: number;
    singlePlayer: boolean;
}

interface PitDto {
    pitId: number;
    stones: number;
    bigPit: boolean;
    position: number;
    player: PlayerEnum;
}

type PlayerEnum = "P1" | "P2" | "P3" | "P4";
