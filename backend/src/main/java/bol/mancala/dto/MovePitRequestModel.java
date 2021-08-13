package bol.mancala.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MovePitRequestModel {

    @NotNull(message = "gameId may not be null")
    private Long gameId;

    @NotNull(message = "positionClicked may not be null")
    private Integer positionClicked;

    @NotNull(message = "playerWhoMoved may not be null")
    public String playerWhoMoved;

}
