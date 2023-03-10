package com.stelmach.bartosz.entity.piece;

import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceType.QUEEN;


public class Queen extends Piece {
    public Queen(int rank, int file, PieceColour colour) {
        super(rank, file, QUEEN, colour, true, Range.between(0,7), Range.between(0,7));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return absRankChange + absFileChange != 0 &&
                (absRankChange == 0 || absFileChange == 0) || absRankChange == absFileChange;
    }
}
