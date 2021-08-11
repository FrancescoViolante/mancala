package bol.mancala.model;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long playerId;

    @NonNull
    private String username;

    @NonNull
    private String password;
}
