package bol.mancala.expected.input;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;

public class MovePitRequestModelInp {

    public static MovePitRequestModel createMovePitRequestModelFirstMove() {

        return new MovePitRequestModel(1L, 4, PlayerEnum.P1);

    }

    public static MovePitRequestModel createMovePitRequestModelP1StealsStones() {

        return new MovePitRequestModel(1L, 0, PlayerEnum.P1);

    }

    public static MovePitRequestModel createMovePitRequestModelP2StealsStones() {

        return new MovePitRequestModel(1L, 7, PlayerEnum.P2);

    }
}
