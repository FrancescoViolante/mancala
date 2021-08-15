package bol.mancala.dto;

import bol.mancala.dto.enums.PlayerEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MovePitRequestModel {

    @NotNull(message = "gameId may not be null")
    private Long gameId;

    @NotNull(message = "positionClicked may not be null")
    private Integer positionClicked;

    @NotNull(message = "playerWhoMoved may not be null")
    public PlayerEnum playerWhoMoved;

}
