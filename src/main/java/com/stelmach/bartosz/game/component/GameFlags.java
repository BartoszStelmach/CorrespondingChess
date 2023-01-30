package com.stelmach.bartosz.game.component;

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
	private boolean isWhiteToMove = true;
	private boolean hasWhiteKingMoved = false;
	private boolean hasBlackKingMoved = false;
	@Column(name = "has_white_a_rook_moved")
	private boolean hasWhiteARookMoved = false;
	@Column(name = "has_white_h_rook_moved")
	private boolean hasWhiteHRookMoved = false;
	@Column(name = "has_black_a_rook_moved")
	private boolean hasBlackARookMoved = false;
	@Column(name = "has_black_h_rook_moved")
	private boolean hasBlackHRookMoved = false;
	private boolean isWhiteKingChecked = false;
	private boolean isBlackKingChecked = false;
	private boolean isSomethingAttackingWhiteShortCastleSquares = false;
	private boolean isSomethingAttackingWhiteLongCastleSquares = false;
	private boolean isSomethingAttackingBlackShortCastleSquares = false;
	private boolean isSomethingAttackingBlackLongCastleSquares = false;

	public GameFlags(Game game) {
		this.game = game;
	}
}
