package bol.mancala.mappers;


import bol.mancala.dto.PitDto;
import bol.mancala.entities.Pit;
import org.mapstruct.Mapper;

@Mapper
public interface PitMapper {

    PitDto pitToPitDto(Pit pit);
}
