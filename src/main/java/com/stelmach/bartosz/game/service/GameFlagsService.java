package com.stelmach.bartosz.game.service;

import com.stelmach.bartosz.board.component.Coordinates;
import com.stelmach.bartosz.board.service.BoardLegalityService;
import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.component.GameFlags;
import com.stelmach.bartosz.game.repository.GameFlagsRepository;
import com.stelmach.bartosz.move.component.MoveDetails;
import com.stelmach.bartosz.piece.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceColour.BLACK;
import static com.stelmach.bartosz.board.component.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.board.component.Board.PieceType.KING;
import static com.stelmach.bartosz.board.component.Board.PieceType.ROOK;
import static com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;
import static com.stelmach.bartosz.move.component.MoveDetails.CastlingType;
import static com.stelmach.bartosz.move.component.MoveDetails.CastlingType.LONG;
import static com.stelmach.bartosz.move.component.MoveDetails.CastlingType.SHORT;

@Service
public class GameFlagsService {
    @Autowired
    private BoardLegalityService boardLegalityService;
    @Autowired
    private GameFlagsRepository gameFlagsRepository;

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
