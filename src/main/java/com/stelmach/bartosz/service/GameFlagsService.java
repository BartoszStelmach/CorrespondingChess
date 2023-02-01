package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.Coordinates;
import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.GameFlags;
import com.stelmach.bartosz.entity.MoveDetails;
import com.stelmach.bartosz.entity.piece.Piece;
import com.stelmach.bartosz.repository.GameFlagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;
import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceColour.BLACK;
import static com.stelmach.bartosz.entity.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.entity.Board.PieceType.KING;
import static com.stelmach.bartosz.entity.Board.PieceType.ROOK;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType.LONG;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType.SHORT;

@Service
public class GameFlagsService {
    @Autowired
    public GameFlagsService(BoardLegalityService boardLegalityService, GameFlagsRepository gameFlagsRepository) {
        this.boardLegalityService = boardLegalityService;
        this.gameFlagsRepository = gameFlagsRepository;
    }

    private final BoardLegalityService boardLegalityService;
    private final GameFlagsRepository gameFlagsRepository;

    public GameFlags updateGameFlags(GameFlags gameFlags, MoveDetails moveDetails, Piece pieceToMove){
        gameFlags.setWhiteToMove(moveDetails.getColour() != WHITE);

        if(moveDetails.getColour() == WHITE) setFlagsForWhite(gameFlags, moveDetails, pieceToMove);
        if(moveDetails.getColour() == BLACK) setFlagsForBlack(gameFlags, moveDetails, pieceToMove);

        return gameFlags;
    }

    private void setFlagsForWhite(GameFlags gameFlags, MoveDetails moveDetails, Piece pieceToMove) {
        if(pieceToMove.getType() == KING) gameFlags.setHasWhiteKingMoved(true);
        else if(pieceToMove.getType() == ROOK && pieceToMove.getCoordinates().getRank() == 0 && pieceToMove.getCoordinates().getFile() == 0) gameFlags.setHasWhiteARookMoved(true);
        else if(pieceToMove.getType() == ROOK && pieceToMove.getCoordinates().getRank() == 0 && pieceToMove.getCoordinates().getFile() == 7) gameFlags.setHasWhiteHRookMoved(true);

        if(moveDetails.isChecking()) gameFlags.setBlackKingChecked(true);

        if(isSomethingAttackingCastlingSquares(WHITE, SHORT, pieceToMove, moveDetails.getEndCoordinates())) gameFlags.setSomethingAttackingWhiteShortCastleSquares(true);
        if(isSomethingAttackingCastlingSquares(WHITE, LONG, pieceToMove, moveDetails.getEndCoordinates())) gameFlags.setSomethingAttackingWhiteLongCastleSquares(true);
    }

    private void setFlagsForBlack(GameFlags gameFlags, MoveDetails moveDetails, Piece pieceToMove) {
        if(pieceToMove.getType() == KING ) gameFlags.setHasBlackKingMoved(true);
        else if(pieceToMove.getType() == ROOK && pieceToMove.getCoordinates().getRank() == 7 && pieceToMove.getCoordinates().getFile() == 0) gameFlags.setHasBlackARookMoved(true);
        else if(pieceToMove.getType() == ROOK && pieceToMove.getCoordinates().getRank() == 7 && pieceToMove.getCoordinates().getFile() == 7) gameFlags.setHasBlackHRookMoved(true);

        if(moveDetails.isChecking()) gameFlags.setWhiteKingChecked(true);

        if(isSomethingAttackingCastlingSquares(BLACK, SHORT, pieceToMove, moveDetails.getEndCoordinates())) gameFlags.setSomethingAttackingWhiteShortCastleSquares(true);
        if(isSomethingAttackingCastlingSquares(BLACK, LONG, pieceToMove, moveDetails.getEndCoordinates())) gameFlags.setSomethingAttackingWhiteLongCastleSquares(true);
    }

    private boolean isSomethingAttackingCastlingSquares(PieceColour colour, CastlingType castlingType, Piece potentiallyAttackingPiece, Coordinates pieceCoordinates) {
        int rank = colour == WHITE ? 0 : 7;
        int lowerFile = castlingType == SHORT ? 4 : 1;
        int higherFile = lowerFile + 3;

        return boardLegalityService.getSquaresBetweenTwoSquares(new Coordinates(rank, lowerFile), new Coordinates(rank,higherFile))
                .stream()
                .anyMatch(s -> potentiallyAttackingPiece.canItReachTheCoordinates(pieceCoordinates, s));
    }

    public GameFlags getGameFlags(Game game) {
        return gameFlagsRepository.findByGame(game).orElseThrow(() -> new ChessAppException("GameFlags not found for the game: " + game));
    }
}
