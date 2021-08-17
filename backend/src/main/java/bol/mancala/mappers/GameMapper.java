package bol.mancala.mappers;

import bol.mancala.dto.GameDto;
import bol.mancala.entities.Game;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(uses = {PitMapper.class}, componentModel = "spring")
public interface GameMapper {

    //GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    GameDto gameToGameDto(Game car);
}

