package bol.mancala.controllers;

import bol.mancala.dto.GameDto;
import bol.mancala.dto.MovePitRequestModel;
import bol.mancala.expected.input.MovePitRequestModelInp;
import bol.mancala.expected.output.GameRes;
import bol.mancala.mappers.GameMapper;
import bol.mancala.services.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameControllerTest {


    private final static String URI = "/mancala";
    private final GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @MockBean
    private GameService gameService;


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }


    @Test
    void createGame() throws Exception {

        GameDto expected = gameMapper.gameToGameDto(GameRes.createNewGameWithTwoPlayers());
        String jsonInString = objectMapper.writeValueAsString(expected);

        when(gameService.initializeBoard(2)).thenReturn(GameRes.createNewGameWithTwoPlayers());
        when(gameService.saveOrUpdateGameInDataBase(any())).thenReturn(expected);

        mockMvc.perform(get(URI + "/new-game")).andExpect(status().isOk())
                .andExpect(content().json(jsonInString))
                .andDo(print());
    }


    @Test
    public void moveStones_InvalidRequestBody() throws Exception {


        MovePitRequestModel modelPitFirstTurn = new MovePitRequestModel();
        modelPitFirstTurn.setGameId(1L);
        String movePitReq = objectMapper.writeValueAsString(modelPitFirstTurn);

        mockMvc.perform(post(URI + "/move-stones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movePitReq))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }


    @Test
    public void moveStonesFirstTurnPlayer1Moves() throws Exception {

        String movePitReq = objectMapper.writeValueAsString(MovePitRequestModelInp.createMovePitRequestModelFirstMove());
        GameDto expected = gameMapper.gameToGameDto(GameRes.gameWithTwoPlayersClickOnPositionFourFirstTurnExpected());
        final String jsonContent = objectMapper.writeValueAsString(expected);

        when(gameService.moveStones(any())).thenReturn(GameRes.gameWithTwoPlayersClickOnPositionFourFirstTurnExpected());
        when(gameService.saveOrUpdateGameInDataBase(any())).thenReturn(expected);

        mockMvc.perform(post(URI + "/move-stones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(movePitReq))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(jsonContent))
                .andDo(print());

    }
}
