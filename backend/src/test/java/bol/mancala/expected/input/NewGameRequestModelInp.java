package bol.mancala.expected.input;

import bol.mancala.dto.NewGameRequestModel;

public class NewGameRequestModelInp {

    public static NewGameRequestModel createNewGameMultiplayer() {

        return new NewGameRequestModel(2, false);

    }

    public static NewGameRequestModel createNewGameSingleplayer() {

        return new NewGameRequestModel(2, true);

    }
}
