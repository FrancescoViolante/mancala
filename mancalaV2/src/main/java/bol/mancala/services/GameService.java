package bol.mancala.services;

import bol.mancala.dto.Keyboard;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public Keyboard createNewGame(int playerAmount){

        return Keyboard.builder().build().initialize(playerAmount);
    }

}
