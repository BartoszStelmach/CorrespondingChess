package com.stelmach.bartosz.entity;

import com.stelmach.bartosz.entity.piece.Piece;
import lombok.*;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.entity.Board.PieceType;
import static com.stelmach.bartosz.entity.piece.Pawn.PawnMoveType;
import static com.stelmach.bartosz.entity.piece.Pawn.PawnMoveType.EN_PASSANT;

@Getter @Setter
@ToString
@AllArgsConstructor @NoArgsConstructor
public class MoveDetails {
    private String notation;
    private PieceColour colour;
    private PieceType pieceType;
    private boolean taking;
    private boolean checking;
    private boolean mating;
    private CastlingType castlingType;
    private boolean ambiguous;
    private Coordinates endCoordinates;
    private Piece pieceToMove = null;
    private Coordinates coordinatesToBeTaken;
    private PawnMoveType pawnMoveType = null;
    private boolean promoting;
    private PieceType promotionPieceType;

    public MoveDetails(String notation, PieceColour colour, PieceType pieceType, boolean taking, boolean checking, boolean mating, CastlingType castlingType, boolean ambiguous, Coordinates endCoordinates) {
        this.notation = notation;
        this.colour = colour;
        this.pieceType = pieceType;
        this.taking = taking;
        this.checking = checking;
        this.mating = mating;
        this.castlingType = castlingType;
        this.ambiguous = ambiguous;
        this.endCoordinates = endCoordinates;
        this.coordinatesToBeTaken = endCoordinates;
    }
    public MoveDetails(BasicMoveDetails basicMoveDetails, SpecificMoveDetails specificMoveDetails, Coordinates endCoordinates) {
        this(basicMoveDetails.notation, basicMoveDetails.colour, basicMoveDetails.pieceType, specificMoveDetails.isTaking, basicMoveDetails.isChecking, basicMoveDetails.isMating, specificMoveDetails.castlingType, basicMoveDetails.isAmbiguous, endCoordinates);
        this.promoting = specificMoveDetails.isPromoting;
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


