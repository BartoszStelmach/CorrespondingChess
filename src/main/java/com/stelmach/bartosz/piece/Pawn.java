package com.stelmach.bartosz.piece;

import com.stelmach.bartosz.board.component.Coordinates;
import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.board.component.Board.PieceType.PAWN;
import static com.stelmach.bartosz.piece.Pawn.PawnMoveType.*;

public class Pawn extends Piece {
    public Pawn(int rank, int file, PieceColour colour) {
        super(rank, file, PAWN, colour, true, Range.between(1,2), Range.between(0,1));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return absRankChange + absFileChange != 3;
    }

    public PawnMoveType determineMoveType(Coordinates endCoordinates, boolean isTaking) {
        if(isTaking) {
            if(canTakeMove(endCoordinates)) return TAKING;
            else throw new IllegalArgumentException("Invalid taking pawn move.");
        }
        if(canNormalMove(endCoordinates)) return NORMAL;
        if(canFirstMove(endCoordinates)) return FIRST;
        throw new IllegalArgumentException("Unexpected pawn move type.");
    }

    public boolean canNormalMove(Coordinates endCoordinates) {
        int expectedRankChange = colour == WHITE ? 1 : -1;
        return endCoordinates.getRank() - getRank() == expectedRankChange && endCoordinates.getFile() - getFile() == 0;
    }

    public boolean canFirstMove(Coordinates endCoordinates) {
        int startingRank = colour == WHITE ? 1 : 6;
        int expectedRankChange = colour == WHITE ? 2 : -2;
        return startingRank == getRank() && endCoordinates.getRank() - getRank() == expectedRankChange && endCoordinates.getFile() - getFile() == 0;
    }

    public boolean canTakeMove(Coordinates endCoordinates) {
        int expectedRankChange = colour == WHITE ? 1 : -1;
        return endCoordinates.getRank() - getRank() == expectedRankChange && Math.abs(endCoordinates.getFile() - getFile()) == 1;
    }

    public enum PawnMoveType {
		NORMAL, FIRST, TAKING, EN_PASSANT
	}
}
