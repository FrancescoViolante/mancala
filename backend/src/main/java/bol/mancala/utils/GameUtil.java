package bol.mancala.utils;

import bol.mancala.model.Pit;

import java.util.LinkedList;


public class GameUtil {

    public static void setAfterPitId(LinkedList<Pit> pits) {
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
}
