package com.stelmach.bartosz.move.service;

import com.stelmach.bartosz.board.component.Board;
import com.stelmach.bartosz.board.component.BoardDbWrapper;
import com.stelmach.bartosz.board.component.Coordinates;
import com.stelmach.bartosz.board.service.BoardDbService;
import com.stelmach.bartosz.board.service.BoardLegalityService;
import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.component.GameFlags;
import com.stelmach.bartosz.game.service.GameFlagsService;
import com.stelmach.bartosz.game.service.GameService;
import com.stelmach.bartosz.move.component.Move;
import com.stelmach.bartosz.move.component.MoveDetails;
import com.stelmach.bartosz.piece.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.stelmach.bartosz.board.component.Board.PieceColour;

@Service
public class MovePlayer {
    @Autowired
    private GameService gameService;
    @Autowired
    private BoardDbService boardDbService;
    @Autowired
    private GameFlagsService gameFlagsService;
    @Autowired
    private MoveParser moveParser;
    @Autowired
    private BoardLegalityService boardLegalityService;
    @Autowired
    private MoveService moveService;

    public void playMove(int id, PieceColour colour, String notation) {
        Game game = gameService.getGame(id);
        GameFlags gameFlags = gameFlagsService.getGameFlags(game);
        BoardDbWrapper boardDbWrapper = boardDbService.getBoardDbWrapper(game);
        Board board = boardDbWrapper.getBoard();

        boardLegalityService.verifyColour(colour, gameFlags);
        MoveDetails moveDetails = moveParser.parseMove(notation, colour, board);
        boardLegalityService.verifyLegality(moveDetails, board);
        transformBoard(moveDetails, board);
        gameFlagsService.updateGameFlags(gameFlags, moveDetails, moveDetails.getPieceToMove());

        Move move = new Move(game, moveDetails.getNotation());
        moveService.commitMove(game, move, gameFlags, boardDbWrapper, board);
    }

    private void transformBoard(MoveDetails moveDetails, Board board) {
        Piece pieceToMove = moveDetails.getPieceToMove();
        Coordinates endCoordinates = moveDetails.getEndCoordinates();

        if(moveDetails.isPromoting()) board.changePieceType(pieceToMove, moveDetails.getPromotionPieceType());
        if(moveDetails.isTaking()) board.removePieceFromCoordinates(moveDetails.getCoordinatesToBeTaken());
        board.movePieceToCoordinates(pieceToMove, endCoordinates);
    }



}
