package com.stelmach.bartosz.entity;

import com.stelmach.bartosz.entity.piece.Piece;
import com.stelmach.bartosz.service.BoardInitializer;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@ToString
@Component
public class Board implements Serializable{
	@Getter
	private final Piece[][] pieces = new Piece[8][8];
	@Getter
	private final Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces = new HashMap<>();

	public enum PieceColour implements Serializable {
		WHITE, BLACK;

		public static PieceColour getOppositeColour(PieceColour colour) {
			return colour == WHITE ? BLACK : WHITE;
		}
	}

	@Getter
	public enum PieceType implements Serializable {
		KING("K"), QUEEN("Q"), BISHOP("B"), KNIGHT("N"), ROOK("R"), PAWN("P");

		private final String symbol;

		PieceType(String symbol) {
			this.symbol = symbol;
		}

		public static Optional<PieceType> get(String symbol) {
			return Arrays.stream(PieceType.values()).filter(pieceType -> pieceType.getSymbol().equals(symbol))
					.findFirst();
		}
	}

	public Board() {
		BoardInitializer.initialize(this);
	}

	public Piece getPieceForCoordinates(Coordinates coordinates) {
		return pieces[coordinates.getRank()][coordinates.getFile()];
	}

	public void removePieceFromCoordinates(Coordinates coordinates) {
		removePiece(getPieceForCoordinates(coordinates));
	}

	public void removePiece(Piece piece) {
		pieces[piece.getRank()][piece.getFile()] = null;
		groupedPieces.get(piece.getColour()).get(piece.getType()).remove(piece);
	}

	public void movePieceToCoordinates(Piece piece, Coordinates endCoordinates) {
		pieces[piece.getRank()][piece.getFile()] = null;
		pieces[endCoordinates.getRank()][endCoordinates.getFile()] = piece;
		piece.setRank(endCoordinates.getRank());
		piece.setFile(endCoordinates.getFile());
	}

	public void changePieceType(Piece piece, PieceType type) {
		groupedPieces.get(piece.getColour()).get(type).add(piece);
		groupedPieces.get(piece.getColour()).get(piece.getType()).remove(piece);
		piece.setType(type);
	}

	public List<Piece> getPiecesForPieceType(PieceColour colour, PieceType pieceType) {
		return groupedPieces.get(colour).get(pieceType);
	}

	public String getGraphicalRepresentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("   -------------------------");
		for (int rank = 7; rank>=0; rank--) {
			sb.append("\n");
			sb.append(rank+1);
			sb.append(" | ");
			for (int file = 0; file<=7; file++) {
				Piece piece = pieces[rank][file];
				if (piece == null) sb.append("   ");
				else {
					sb.append(piece.getColour().name().charAt(0));
					sb.append(piece.getType().getSymbol());
					sb.append(" ");
				}
			}
			sb.append("|");
		}
		sb.append("\n   -------------------------");
		sb.append("\n     A  B  C  D  E  F  G  H  ");
		return sb.toString();
	}
}
