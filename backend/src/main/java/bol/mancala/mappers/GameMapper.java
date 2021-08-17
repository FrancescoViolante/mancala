package bol.mancala.mappers;

import bol.mancala.dto.GameDto;
import bol.mancala.dto.PitDto;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(uses = {PitMapper.class})
public interface GameMapper {

    @Mapping(source = "pits", target = "pitP1", qualifiedByName = "player1ToDto")
    @Mapping(source = "pits", target = "pitP2", qualifiedByName = "player2ToDto")
    GameDto gameToGameDto(Game game);

    @Named("player1ToDto")
    default List<PitDto> listPlayer1ToDto(List<Pit> pits) {

        return listToListDto(pits.stream().filter(pit -> pit.getPlayer() == PlayerEnum.P1).collect(Collectors.toList()));
    }

    @Named("player2ToDto")
    default List<PitDto> listPlayer2ToDto(List<Pit> pits) {

        return listToListDto(pits.stream().filter(pit -> pit.getPlayer() == PlayerEnum.P2).collect(Collectors.toList()));
    }

    default List<PitDto> listToListDto(List<Pit> pits) {
        List<PitDto> pitDtoList = new ArrayList<>();
        PitMapper pitMapper = Mappers.getMapper(PitMapper.class);
        pits.forEach(e -> pitDtoList.add(pitMapper.pitToPitDto(e)));
        return pitDtoList;
    }

}

