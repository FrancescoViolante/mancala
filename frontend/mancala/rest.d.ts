/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.27.744 on 2021-08-17 19:08:41.

interface GameDto {
    gameId: number;
    playerAmount: number;
    playerWhoMove: PlayerEnum;
    pitP1: PitDto[];
    pitP2: PitDto[];
    finished: boolean;
}

interface MovePitRequestModel {
    gameId: number;
    positionClicked: number;
    playerWhoMoved: PlayerEnum;
}

interface PitDto {
    pitId: number;
    stones: number;
    bigPit: boolean;
    position: number;
    player: PlayerEnum;
}

type PlayerEnum = "P1" | "P2" | "P3" | "P4";
