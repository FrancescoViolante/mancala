package bol.mancala.mappers;

import bol.mancala.dto.GameDto;
import bol.mancala.entities.Game;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(uses = {PitMapper.class})
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameDto gameToGameDto(Game car);
}

