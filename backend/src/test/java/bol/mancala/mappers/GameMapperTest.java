package bol.mancala.mappers;

import bol.mancala.dto.GameDto;
import bol.mancala.dto.PitDto;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.expected.output.GameRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    private Game game;
    private final GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @BeforeEach
    void setUp() {
        game = GameRes.gameWithTwoPlayersP1WinFinalResultExpected();
    }

    @Test
    void gameToGameDto() {

        GameDto gameDto = gameMapper.gameToGameDto(game);

        assertAll(
                () -> assertThat(gameDto.getGameId()).isEqualTo(1L),
                () -> assertThat(gameDto.getPlayerAmount()).isEqualTo(2),
                () -> assertThat(gameDto.getPlayerWhoMove()).isEqualTo(PlayerEnum.P2),
                () -> assertTrue(gameDto.isFinished())
        );

    }

    @Test
    void listPlayer1ToDto() {

        List<PitDto> listPitDtoP1 = gameMapper.listPlayer1ToDto(game.getPits());

        assertAll(
                () -> assertTrue(listPitDtoP1.stream().allMatch(pit -> pit.getPlayer() == PlayerEnum.P1)),
                executableCountBigPits(listPitDtoP1, 1),
                executablePitListSizeIsEqualTo7(listPitDtoP1, 7)
        );
    }


    private Executable executableCountBigPits(List<PitDto> listPitDtoP1, long bigPitsOccurencies) {
        return () -> assertThat(listPitDtoP1.stream().filter(PitDto::isBigPit).count()).isEqualTo(bigPitsOccurencies);
    }

    @Test
    void listPlayer2ToDto() {
        List<PitDto> listPitDtoP2 = gameMapper.listPlayer2ToDto(game.getPits());

        assertAll(
                () -> assertTrue(listPitDtoP2.stream().allMatch(pit -> pit.getPlayer() == PlayerEnum.P2)),
                executableCountBigPits(listPitDtoP2, 1),
                executablePitListSizeIsEqualTo7(listPitDtoP2, 7)
        );
    }

    private Executable executablePitListSizeIsEqualTo7(List<PitDto> listPitDtoP2, int listSize) {
        return () -> assertThat(listPitDtoP2.size()).isEqualTo(listSize);
    }

    @Test
    void listToListDto() {
        List<PitDto> pitDtoList = gameMapper.listToListDto(game.getPits());

        assertAll(
                executableCountBigPits(pitDtoList, 2),
                executablePitListSizeIsEqualTo7(pitDtoList, 14)
        );
    }
}
