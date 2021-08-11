package bol.mancala.model;

import bol.mancala.dto.enums.PlayerEnum;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "pitId")
@EqualsAndHashCode(exclude = "game")
@ToString(exclude = "game")
public class Pit implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pitId;

    @NonNull
    private int stones;

    @NonNull
    private boolean bigPit;

    private int position;

    @Column(name = "player")
    @Enumerated(EnumType.STRING)
    private PlayerEnum player;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_gameId")
    private Game game;
}
