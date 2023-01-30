package com.stelmach.bartosz.move.component;

import com.stelmach.bartosz.game.component.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Move {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	@ManyToOne
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;
	private String notation;

	public Move(Game game, String notation) {
		this.game = game;
		this.notation = notation;
	}

}
