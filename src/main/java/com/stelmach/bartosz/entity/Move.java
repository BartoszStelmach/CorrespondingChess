package com.stelmach.bartosz.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
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
