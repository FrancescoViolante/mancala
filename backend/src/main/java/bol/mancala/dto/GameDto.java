package bol.mancala.dto;

import bol.mancala.dto.enums.PlayerEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private Long gameId;
    private int playerAmount;
    private PlayerEnum playerWhoMove;

    private List<PitDto> pitP1 = new LinkedList<>();
    private List<PitDto> pitP2 = new LinkedList<>();

    private boolean finished;
}
