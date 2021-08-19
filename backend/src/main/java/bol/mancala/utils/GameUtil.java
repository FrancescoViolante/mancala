package bol.mancala.utils;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Pit;
import bol.mancala.services.GameService;

import java.util.LinkedList;


public class GameUtil {

    public static void setNextPitPosition(LinkedList<Pit> pits) {
        Pit lastPit = pits.peekLast();

        pits.forEach(
                (element) -> {
                    if (!element.equals(lastPit))
                        element.setPositionNextElement(pits.indexOf(element) + 1);
                    else
                        element.setPositionNextElement(0);
                }
        );
    }

    public static  void calculatePlayerEnum(LinkedList<Pit> pits) {
        pits.forEach(pit -> {
            if (pit.getPosition() == GameService.INITIAL_POSITION) {
                pit.setPlayer(PlayerEnum.P1);
            } else {
                int player = pit.getPosition() / GameService.POSITION_TO_ADD + 1;
                pit.setPlayer(PlayerEnum.getPlayerEnumByValue(player));
            }
        });

    }
}
