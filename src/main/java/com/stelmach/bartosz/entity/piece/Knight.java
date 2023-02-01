package com.stelmach.bartosz.entity.piece;

import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceType.KNIGHT;
public class Knight extends Piece {
    public Knight(int rank, int file, PieceColour colour) {
        super(rank, file, KNIGHT, colour, false, Range.between(1,2), Range.between(1,2));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return absRankChange + absFileChange == 3;
    }
}
