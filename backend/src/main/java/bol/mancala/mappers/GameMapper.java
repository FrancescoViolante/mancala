package bol.mancala.mappers;

import bol.mancala.dto.GameDto;
import bol.mancala.dto.PitDto;
import bol.mancala.dto.enums.PlayerEnum;
import bol.mancala.entities.Game;
import bol.mancala.entities.Pit;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(uses = {PitMapper.class}, componentModel = "spring")
public interface GameMapper {

    //GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @Mapping(source = "pits", target = "pitP1", qualifiedByName = "pit1")
    @Mapping(source = "pits", target = "pitP2", qualifiedByName = "pit2")
    GameDto gameToGameDto(Game car);

    @Named("pit1")
    default List<PitDto> locationToLocationDto(List<Pit> pits) {

        List<PitDto> pitDtos = new ArrayList<PitDto>();


        return beforeMappingToDTO(pits.stream().filter(pit -> pit.getPlayer() == PlayerEnum.P1).collect(Collectors.toList()), pitDtos);
    }

    @Named("pit2")
    default List<PitDto> locationToBinType(List<Pit> pits) {

        List<PitDto> pitDtos = new ArrayList<PitDto>();
        return beforeMappingToDTO(pits.stream().filter(pit -> pit.getPlayer() == PlayerEnum.P2).collect(Collectors.toList()), pitDtos);
    }

    @BeforeMapping
    default List<PitDto> beforeMappingToDTO(List<Pit> source, @MappingTarget List<PitDto> target) {

        PitMapper pitMapper = Mappers.getMapper(PitMapper.class);
        source.forEach(e -> target.add(pitMapper.pitToPitDto(e)));
        return target;
    }

}

