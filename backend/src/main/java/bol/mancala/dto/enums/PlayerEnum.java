package bol.mancala.dto.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PlayerEnum {

    P1(1),
    P2(2),
    P3(3),
    P4(4);

    private final int value;

    PlayerEnum(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

    public static PlayerEnum getPlayerEnumByValue(final int value) {

        return Arrays.stream(PlayerEnum.values())
                .filter(playerEnum -> playerEnum.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static PlayerEnum getNextPlayerEnum(final int playerAmount, PlayerEnum playerEnumMoving) {

        List<PlayerEnum> playerEnumInGameSet = Arrays.stream(PlayerEnum.values())
                .limit(playerAmount).collect(Collectors.toUnmodifiableList());

        if (playerEnumInGameSet.get(playerEnumInGameSet.indexOf(playerEnumMoving)).getValue() == playerEnumInGameSet.size())
            return PlayerEnum.P1;
        else return getPlayerEnumByValue(playerEnumMoving.getValue() + 1);

    }
}
