package com.stelmach.bartosz.entity.piece;

import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceType.KING;
public class King extends Piece {
    public King(int rank, int file, PieceColour colour) {
        super(rank, file, KING, colour, true, Range.between(0,1), Range.between(0,1));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return absRankChange + absFileChange != 0;
    }
}
