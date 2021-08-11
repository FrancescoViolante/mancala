package bol.mancala.repositories;

import bol.mancala.model.Pit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitRepo extends JpaRepository<Pit, Long> {

    List<Pit> findAllByGame_GameId(Long gameId);

}
