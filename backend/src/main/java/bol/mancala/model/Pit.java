package bol.mancala.model;

import bol.mancala.dto.enums.PlayerEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Data
@Table( name="PIT" )
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "pitId")
public class Pit {


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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "gameId")
    private Game game;
}
