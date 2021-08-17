package bol.mancala.mappers;


import bol.mancala.dto.PitDto;
import bol.mancala.entities.Pit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PitMapper {

    //PitMapper pitMapper = Mappers.getMapper(PitMapper.class);

    PitDto pitToPitDto(Pit pit);
}
