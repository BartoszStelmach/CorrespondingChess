package com.stelmach.bartosz.board.component;

import com.stelmach.bartosz.game.component.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @NoArgsConstructor
@Getter @Setter
@Table(name = "board")
public class BoardDbWrapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @OneToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @Column(columnDefinition = "LONGBLOB")
    private byte[] boardBytes;
    @Transient
    private Board board;

    public BoardDbWrapper(Game game, byte[] boardBytes) {
        this.game = game;
        this.boardBytes = boardBytes;
    }

    @Override
    public String toString() {
        return "BoardDbWrapper{" +
                "ID=" + ID +
                ", game=" + game +
                '}';
    }
}
