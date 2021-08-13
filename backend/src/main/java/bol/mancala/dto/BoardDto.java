package bol.mancala.dto;


import bol.mancala.model.Pit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BoardDto {


    private LinkedList<Pit> situation;


/*

    private int calculatePitNumber(int players) {
        return players * 6;
    }

    private Keyboard createKeyboard(int pitNumber) {

        for (long i = 0L; i < pitNumber; i++) {

            boolean isBigPit = calculateBigPit(i);

            // situation.add(i, new Pit(i, STONES, isBigPit));

        }
        return setAfterPitId();
    }

    private Keyboard setAfterPitId() {
        Pit lastPit = situation.peekLast();

        situation.forEach(
                (element) -> {
                    if (!element.equals(lastPit))
                        element.setAfterPitPosition(situation.indexOf(element) + 1);
                    else
                        element.setAfterPitPosition(0);
                }
        );
        calculateOwner();
        return this;
    }

    private void calculateOwner() {
        getSituation().forEach(pit -> {
            if (pit.getPitId() == 0) {
                pit.setPlayer("P" + getPlayers());
            } else {
                int player = pit.getPitId().intValue() / 7 + 1;
                pit.setPlayer("P" + player);
            }
        });

    }

    private boolean calculateBigPit(long i) {

        return i % 7 == 0;
    }
*/

}
