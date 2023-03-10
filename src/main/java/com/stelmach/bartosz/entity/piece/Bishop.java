package com.stelmach.bartosz.entity.piece;

import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceType.BISHOP;

public class Bishop extends Piece {
    public Bishop(int rank, int file, PieceColour colour) {
        super(rank, file, BISHOP, colour, true, Range.between(1,7), Range.between(1,7));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return absRankChange == absFileChange;
    }
}
