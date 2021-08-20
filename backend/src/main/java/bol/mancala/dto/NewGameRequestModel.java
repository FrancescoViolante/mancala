package bol.mancala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewGameRequestModel {

    @NotNull(message = "playerWhoMoved may not be null")
    public int playerAmount;

    @NotNull(message = "positionClicked may not be null")
    private boolean singlePlayer;

}
