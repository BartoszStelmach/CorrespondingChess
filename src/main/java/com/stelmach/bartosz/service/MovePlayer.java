package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.*;
import com.stelmach.bartosz.entity.piece.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.stelmach.bartosz.entity.Board.PieceColour;

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
