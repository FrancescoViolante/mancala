package bol.mancala.expected.inputs;

import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.dto.enums.PlayerEnum;

public class MovePitRequestModelImp {

    public static MovePitRequestModel createMovePitRequestModel() {

        return new MovePitRequestModel(1L, 4, PlayerEnum.P1);

    }
}
