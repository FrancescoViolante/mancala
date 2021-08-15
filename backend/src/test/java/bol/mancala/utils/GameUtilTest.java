package bol.mancala.utils;

import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.expected.results.GameRes;
import bol.mancala.entities.Pit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class GameUtilTest {

    static LinkedList<Pit> pits;

    @BeforeAll
    static void beforeAll() {
        try (MockedStatic<GameUtil> utilities = Mockito.mockStatic(GameUtil.class)) {
            utilities.when(() -> GameUtil.setNextPitPosition(any())).thenCallRealMethod();
            utilities.when(() -> GameUtil.calculatePlayerEnum(any())).thenCallRealMethod();
        }
        pits = new LinkedList<>(GameRes.createNewGameWithTwoPlayers().getPits());
    }

    @Test
    void setAfterPitId() {

        GameUtil.setNextPitPosition(pits);

        Assertions.assertAll(
                () -> assertEquals(pits.get(0).getPositionNextElement(), 1),
                () -> assertEquals(pits.get(1).getPositionNextElement(), 2),
                () -> assertEquals(pits.get(2).getPositionNextElement(), 3),
                () -> assertEquals(pits.get(3).getPositionNextElement(), 4),
                () -> assertEquals(pits.get(4).getPositionNextElement(), 5),
                () -> assertEquals(pits.get(5).getPositionNextElement(), 6),
                () -> assertEquals(pits.get(6).getPositionNextElement(), 7),
                () -> assertEquals(pits.get(7).getPositionNextElement(), 8),
                () -> assertEquals(pits.get(8).getPositionNextElement(), 9),
                () -> assertEquals(pits.get(9).getPositionNextElement(), 10),
                () -> assertEquals(pits.get(10).getPositionNextElement(), 11),
                () -> assertEquals(pits.get(11).getPositionNextElement(), 12),
                () -> assertEquals(pits.get(12).getPositionNextElement(), 13),
                () -> assertEquals(pits.get(13).getPositionNextElement(), 0)
        );
    }

    @Test
    void calculatePlayerEnum() {
        GameUtil.calculatePlayerEnum(pits);

        LinkedList<Pit> fourPlayerPitList = GameRes.createPitListFourPlayers();

        GameUtil.calculatePlayerEnum(fourPlayerPitList);
        Assertions.assertAll(
                () -> assertTrue(firstElementPlayerEnumIsPlayerP1(pits)),
                () -> assertTrue(lastPlayerEnumElementsInTwoPlayerGameArePlayerP2(pits)),
                () -> assertTrue(playerEnumElementsFrom15To21ArePlayerP3(fourPlayerPitList)),
                () -> assertTrue(playerEnumElementsFrom22To28ArePlayerP4(fourPlayerPitList))
        );
    }

    private boolean firstElementPlayerEnumIsPlayerP1(LinkedList<Pit> pits) {
        return pits.stream()
                .limit(7).map(Pit::getPlayer)
                .allMatch(player -> player.equals(PlayerEnum.P1));
    }

    private boolean lastPlayerEnumElementsInTwoPlayerGameArePlayerP2(LinkedList<Pit> pits) {
        return pits.stream()
                .skip(7).map(Pit::getPlayer)
                .allMatch(player -> player.equals(PlayerEnum.P2));
    }

    private boolean playerEnumElementsFrom15To21ArePlayerP3(LinkedList<Pit> pits) {
        return pits.stream()
                .skip(14).map(Pit::getPlayer)
                .limit(7)
                .allMatch(player -> player.equals(PlayerEnum.P3));
    }

    private boolean playerEnumElementsFrom22To28ArePlayerP4(LinkedList<Pit> pits) {
        return pits.stream()
                .skip(21).map(Pit::getPlayer)
                .allMatch(player -> player.equals(PlayerEnum.P4));
    }
}
