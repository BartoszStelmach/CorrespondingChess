package com.stelmach.bartosz.board.component;

import com.stelmach.bartosz.game.component.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity @NoArgsConstructor
@Getter @Setter @ToString
@Table(name = "board")
public class BoardDbWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @OneToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    private byte[] boardBytes;
    @Transient
    private Board board;

    public BoardDbWrapper(Game game, byte[] boardBytes) {
        this.game = game;
        this.boardBytes = boardBytes;
    }
}
