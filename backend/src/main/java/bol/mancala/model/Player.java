package bol.mancala.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long playerId;

    @NonNull
    private String username;

    @NonNull
    private String password;
}
