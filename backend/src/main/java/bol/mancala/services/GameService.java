package bol.mancala.services;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.model.Game;

public interface GameService {

    Game initializeBoard(int playerAmount);

    Game moveStones(MovePitRequestModel movePitRequestModel);
}
