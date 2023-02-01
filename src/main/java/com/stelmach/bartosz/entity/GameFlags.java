package com.stelmach.bartosz.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
public class GameFlags {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	@OneToOne
	@JoinColumn(name = "game_id", nullable = false)
	private Game game;
	private boolean whiteToMove = true;
	private boolean whiteKingMoved = false;
	private boolean blackKingMoved = false;
	private boolean whiteFirstFileRookMoved = false;
	private boolean whiteLastFileRookMoved = false;
	private boolean blackFirstFileRookMoved = false;
	private boolean blackLastFileRookMoved = false;
	private boolean whiteKingChecked = false;
	private boolean blackKingChecked = false;
	private boolean somethingAttackingWhiteShortCastleSquares = false;
	private boolean somethingAttackingWhiteLongCastleSquares = false;
	private boolean somethingAttackingBlackShortCastleSquares = false;
	private boolean somethingAttackingBlackLongCastleSquares = false;

	public GameFlags(Game game) {
		this.game = game;
	}
}
