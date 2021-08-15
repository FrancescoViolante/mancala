package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.entities.Game;

public interface GameService {


    int FIRST_PLAYER = 1;
    int POSITION_TO_ADD = 7;
    int FIRT_BIGPIT_POSITION = 6;
    int INITIAL_STONES_PIT = 6;
    int INITIAL_STONES_BIGPIT = 0;
    int INITIAL_POSITION = 0;
    int INCREMENT_STONE = 1;
    long POSITION_CLICKED_IS_PRESENT = 1L;

    Game initializeBoard(int playerAmount);

    Game moveStones(MovePitRequestModel movePitRequestModel);
}
