package com.stelmach.bartosz.piece;

import com.stelmach.bartosz.board.component.Coordinates;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Range;

import java.io.Serializable;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceType;
import static com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;

@Getter @Setter
public abstract class Piece implements Serializable {
	protected Coordinates coordinates;
	protected PieceType type;
	protected final PieceColour colour;
	protected final boolean isBlockable;
	protected final Range<Integer> allowedRankChange;
	protected final Range<Integer> allowedFileChange;

	public boolean canItReachTheCoordinates(Coordinates coordinates) {
		return canItReachTheCoordinates(coordinates, this.coordinates);
	}

	public boolean canItReachTheCoordinates(Coordinates startCoordinates, Coordinates endCoordinates) {
		int absRankChange = Math.abs(endCoordinates.getRank() - startCoordinates.getRank());
		int absFileChange = Math.abs(endCoordinates.getFile() - startCoordinates.getFile());

		return areCoordinatesChangesInRange(absRankChange, absFileChange) &&
				areSpecificConditionsSatisfied(absRankChange, absFileChange);
	}

	public Piece(int rank, int file, PieceType type, PieceColour colour, boolean isBlockable, Range<Integer> allowedRankChange, Range<Integer> allowedFileChange) {
		this.coordinates = new Coordinates(rank, file);
		this.type = type;
		this.colour = colour;
		this.isBlockable = isBlockable;
		this.allowedRankChange = allowedRankChange;
		this.allowedFileChange = allowedFileChange;
	}

	public int getRank() {
		return coordinates.getRank();
	}

	public int getFile() {
		return coordinates.getFile();
	}

	public void setRank(int rank) {
		if (rank < 0 || rank > 7) throw new ChessAppException("Piece's rank should be in the range <0, 7>");
		this.coordinates.setRank(rank);
	}

	public void setFile(int file) {
		if (file < 0 || file > 7) throw new ChessAppException("Piece's file should be in the range <0, 7>");
		this.coordinates.setFile(file);
	}

	@Override
	public String toString() {
		return "Piece{" +
				"coordinates=" + coordinates +
				", type=" + type +
				", colour=" + colour +
				", isBlockable=" + isBlockable +
				", allowedRankChange=" + allowedRankChange +
				", allowedFileChange=" + allowedFileChange +
				'}';
	}

	public boolean areCoordinatesChangesInRange(int absRankChange, int absFileChange) {
		return  allowedRankChange.contains(absRankChange) && allowedFileChange.contains(absFileChange);
	}

	public abstract boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange);
}
