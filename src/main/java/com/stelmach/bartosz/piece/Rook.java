package com.stelmach.bartosz.piece;

import org.apache.commons.lang3.Range;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceType.ROOK;

public class Rook extends Piece {
    public Rook(int rank, int file, PieceColour colour) {
        super(rank, file, ROOK, colour, true, Range.between(0,7), Range.between(0,7));
    }

    @Override
    public boolean areSpecificConditionsSatisfied(int absRankChange, int absFileChange) {
        return (absRankChange == 0 || absFileChange == 0)
                && absRankChange + absFileChange != 0;
    }
}
