package bol.mancala.utils;

import bol.mancala.expected.results.GameRes;
import bol.mancala.entities.Pit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameUtilTest {

    @Test
    void setAfterPitId() {

        LinkedList<Pit> pits = new LinkedList<>(GameRes.createNewGameWithTwoPlayers().getPits());
        try (MockedStatic<GameUtil> utilities = Mockito.mockStatic(GameUtil.class)) {
            utilities.when(() -> GameUtil.setAfterPitId(pits)).thenCallRealMethod();
        }
        GameUtil.setAfterPitId(pits);

        Assertions.assertAll(
                () ->assertEquals(pits.get(0).getPositionNextElement(), 1),
                () ->assertEquals(pits.get(1).getPositionNextElement(), 2),
                () ->assertEquals(pits.get(2).getPositionNextElement(), 3),
                () ->assertEquals(pits.get(3).getPositionNextElement(), 4),
                () ->assertEquals(pits.get(4).getPositionNextElement(), 5),
                () ->assertEquals(pits.get(5).getPositionNextElement(), 6),
                () ->assertEquals(pits.get(6).getPositionNextElement(), 7),
                () ->assertEquals(pits.get(7).getPositionNextElement(), 8),
                () ->assertEquals(pits.get(8).getPositionNextElement(), 9),
                () ->assertEquals(pits.get(9).getPositionNextElement(), 10),
                () ->assertEquals(pits.get(10).getPositionNextElement(), 11),
                () ->assertEquals(pits.get(11).getPositionNextElement(), 12),
                () ->assertEquals(pits.get(12).getPositionNextElement(), 13),
                () ->assertEquals(pits.get(13).getPositionNextElement(), 0)
        );
        assertEquals(pits.get(0).getPositionNextElement(), 1);
    }
}
