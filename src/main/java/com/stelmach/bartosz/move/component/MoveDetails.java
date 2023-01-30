package com.stelmach.bartosz.move.component;

import com.stelmach.bartosz.board.component.Coordinates;
import com.stelmach.bartosz.piece.Piece;
import lombok.*;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.board.component.Board.PieceType;
import static com.stelmach.bartosz.piece.Pawn.PawnMoveType;
import static com.stelmach.bartosz.piece.Pawn.PawnMoveType.EN_PASSANT;

@Getter @Setter
@ToString
@AllArgsConstructor @NoArgsConstructor
public class MoveDetails {
    protected String notation;
    protected PieceColour colour;
    protected PieceType pieceType;
    protected boolean isTaking;
    protected boolean isChecking;
    protected boolean isMating;
    protected CastlingType castlingType;
    protected boolean isAmbiguous;
    protected Coordinates endCoordinates;
    protected Piece pieceToMove = null;
    protected Coordinates coordinatesToBeTaken;
    protected PawnMoveType pawnMoveType = null;
    protected boolean isPromoting;
    protected PieceType promotionPieceType;

    public MoveDetails(String notation, PieceColour colour, PieceType pieceType, boolean isTaking, boolean isChecking, boolean isMating, CastlingType castlingType, boolean isAmbiguous, Coordinates endCoordinates) {
        this.notation = notation;
        this.colour = colour;
        this.pieceType = pieceType;
        this.isTaking = isTaking;
        this.isChecking = isChecking;
        this.isMating = isMating;
        this.castlingType = castlingType;
        this.isAmbiguous = isAmbiguous;
        this.endCoordinates = endCoordinates;
        this.coordinatesToBeTaken = endCoordinates;
    }
    public MoveDetails(BasicMoveDetails basicMoveDetails, SpecificMoveDetails specificMoveDetails, Coordinates endCoordinates) {
        this(basicMoveDetails.notation, basicMoveDetails.colour, basicMoveDetails.pieceType, specificMoveDetails.isTaking, basicMoveDetails.isChecking, basicMoveDetails.isMating, specificMoveDetails.castlingType, basicMoveDetails.isAmbiguous, endCoordinates);
        this.isPromoting = specificMoveDetails.isPromoting;
        this.promotionPieceType = specificMoveDetails.promotionPieceType;
    }

    public void setPawnMoveType(PawnMoveType pawnMoveType) {
        this.pawnMoveType = pawnMoveType;
        if(pawnMoveType == EN_PASSANT) coordinatesToBeTaken = getEnPassantTakingCoordinates();
    }

    public Coordinates getEnPassantTakingCoordinates() {
        int expectedEndRankChange = colour == WHITE ? -1 : 1;
        return new Coordinates(endCoordinates.getRank() + expectedEndRankChange, endCoordinates.getFile());
    }

    public enum CastlingType {
        SHORT, LONG
    }
    @Getter @AllArgsConstructor
    public static class BasicMoveDetails {
        protected PieceType pieceType;
        protected String notation;
        protected PieceColour colour;
        protected boolean isChecking;
        protected boolean isMating;
        protected boolean isAmbiguous;
    }

    @Getter @AllArgsConstructor
    public static class SpecificMoveDetails {
        protected boolean isTaking;
        protected boolean isPromoting;
        protected CastlingType castlingType;
        protected PieceType promotionPieceType;
    }
}


