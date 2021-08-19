package bol.mancala.dto;

import bol.mancala.dto.enums.PlayerEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PitDto {


    private Long pitId;
    private int stones;
    private boolean bigPit;
    private int position;
    private PlayerEnum player;
}
