package com.stelmach.bartosz.board.service;

import com.stelmach.bartosz.board.component.Board;
import com.stelmach.bartosz.board.component.Coordinates;
import com.stelmach.bartosz.game.component.GameFlags;
import com.stelmach.bartosz.move.component.MoveDetails;
import com.stelmach.bartosz.piece.Piece;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceColour.BLACK;
import static com.stelmach.bartosz.board.component.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.board.component.Board.PieceType.KING;
import static com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;

@Service
public class BoardLegalityService {
	public void verifyLegality(MoveDetails moveDetails, Board board) {
		Piece piece = moveDetails.getPieceToMove();
		Coordinates endCoordinates = moveDetails.getEndCoordinates();
		verifyMoveChecking(moveDetails.isChecking(), piece, endCoordinates, board);
		verifyMoveTaking(moveDetails.isTaking(), endCoordinates, board);
		verifyObstacles(piece, endCoordinates, board);
	}

	public void verifyColour(PieceColour requestedColour, GameFlags gameFlags) {
		PieceColour colourToMove = gameFlags.isWhiteToMove() ? WHITE : BLACK;
		if (requestedColour != colourToMove) throw new IllegalArgumentException("It's not " + requestedColour + "'s move.");
	}

	public void verifyMoveChecking(boolean isNotationChecking, Piece pieceToMove, Coordinates endCoordinates, Board board) {
		Piece opponentKing = board.getPiecesForPieceType(PieceColour.getOppositeColour(pieceToMove.getColour()), KING).get(0);
		boolean isOpponentKingReachable = pieceToMove.canItReachTheCoordinates(endCoordinates, opponentKing.getCoordinates());
		if (isNotationChecking && !isOpponentKingReachable) throw new IllegalArgumentException("The move is not checking.");
		if (!isNotationChecking && isOpponentKingReachable) throw new IllegalArgumentException("The move should be checking.");
	}

	public void verifyMoveTaking(boolean isNotationTaking, Coordinates endCoordinates, Board board) {
		if(isNotationTaking != isPieceOnSquare(endCoordinates, board)) throw new IllegalArgumentException("Can't take empty square or can't move to occupied square.");
	}

	public boolean isPieceOnSquare(Coordinates coordinates, Board board) {
		return board.getPieceForCoordinates(coordinates) != null;
	}

	public void verifyObstacles(Piece piece, Coordinates endCoordinates, Board board) {
		if((piece.isBlockable() && isTherePieceBetweenSquaresOnStraightLine(piece.getCoordinates(), endCoordinates, board))) throw new IllegalArgumentException("Destination square is blocked for this piece.");
	}

	public boolean isTherePieceBetweenSquaresOnStraightLine(Coordinates coordinates1, Coordinates coordinates2, Board board) {
		List<Coordinates> coordinatesList = getSquaresBetweenTwoSquares(coordinates1, coordinates2);
		return coordinatesList.stream().map(board::getPieceForCoordinates).anyMatch(Objects::nonNull);
	}

	public List<Coordinates> getSquaresBetweenTwoSquares(Coordinates coordinates1, Coordinates coordinates2) {
		RelativePositionLine lineType = RelativePositionLine.get(coordinates1, coordinates2);

		int rankLow = Math.min(coordinates1.getRank(), coordinates2.getRank());
		int rankHigh = Math.max(coordinates1.getRank(), coordinates2.getRank());
		int fileLow = Math.min(coordinates1.getFile(), coordinates2.getFile());
		int fileHigh = Math.max(coordinates1.getFile(), coordinates2.getFile());

		if (lineType == RelativePositionLine.CURVE) throw new ChessAppException("Coordinates are not on the straight line - coordinates1: " + coordinates1 + ", coordinates2: " + coordinates2);
		if (lineType == RelativePositionLine.HORIZONTAL) return convertToCoordinates(fileLow+1, fileHigh, i -> new Coordinates(rankLow,i));
		if (lineType == RelativePositionLine.VERTICAL) return convertToCoordinates(rankLow+1, rankHigh, i -> new Coordinates(i, fileLow));
		if (lineType == RelativePositionLine.DIAGONAL_UPWARD) return convertToCoordinates(rankLow+1, rankHigh, i -> new Coordinates(i, fileLow - rankLow + i));
		if (lineType == RelativePositionLine.DIAGONAL_DOWNWARD) return convertToCoordinates(rankLow+1, rankHigh, i -> new Coordinates(i, fileHigh + rankLow - i));
		throw new ChessAppException("Couldn't form a line for given coordinates - coordinates1: " + coordinates1 + ", coordinates2: " + coordinates2);
	}

	private List<Coordinates> convertToCoordinates(int rangeStart, int rangeEnd, IntFunction<Coordinates> function){
		return IntStream.range(rangeStart, rangeEnd).mapToObj(function).collect(Collectors.toList());
	}

	public enum RelativePositionLine {
		HORIZONTAL, VERTICAL, DIAGONAL_UPWARD, DIAGONAL_DOWNWARD, CURVE;

		private static RelativePositionLine get(Coordinates coordinates1, Coordinates coordinates2) {
			int rank1 = coordinates1.getRank();
			int rank2 = coordinates2.getRank();
			int file1 = coordinates1.getFile();
			int file2 = coordinates2.getFile();

			if (rank1 == rank2 && file1 != file2) return HORIZONTAL;
			if (rank1 != rank2 && file1 == file2) return VERTICAL;
			if (Math.abs(rank1 - rank2) != Math.abs(file1 - file2)) return CURVE;
			if (rank1 + file1 == rank2 + file2) return DIAGONAL_DOWNWARD;
			else return DIAGONAL_UPWARD;
		}
	}
}
