package bol.mancala.mappers;

import bol.mancala.dto.PitDto;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Pit;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PitMapperTest {

    @Test
    void pitToPitDto() {

        Pit pit = Pit.builder()
                .pitId(1L).stones(7)
                .position(2).bigPit(false)
                .player(PlayerEnum.P1)
                .positionNextElement(3)
                .build();

        PitMapper pitMapper = Mappers.getMapper(PitMapper.class);
        PitDto pitDto = pitMapper.pitToPitDto(pit);

        assertAll(
                () -> assertThat(pitDto.getPitId()).isEqualTo(1L),
                () -> assertThat(pitDto.getStones()).isEqualTo(7),
                () -> assertThat(pitDto.getPosition()).isEqualTo(2),
                () -> assertFalse(pitDto.isBigPit()),
                () -> assertThat(pitDto.getPlayer()).isEqualTo(PlayerEnum.P1)
        );

    }
}
