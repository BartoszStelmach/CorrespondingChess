package com.stelmach.bartosz.controller;

import com.stelmach.bartosz.entity.Board.PieceColour;
import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.service.BoardDbService;
import com.stelmach.bartosz.service.GameCreator;
import com.stelmach.bartosz.service.GameService;
import com.stelmach.bartosz.service.MovePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
	@Autowired
	public ChessController(GameCreator gameCreator, GameService gameService, MovePlayer movePlayer, BoardDbService boardDbService) {
		this.gameCreator = gameCreator;
		this.gameService = gameService;
		this.movePlayer = movePlayer;
		this.boardDbService = boardDbService;
	}

	private final GameCreator gameCreator;
	private final GameService gameService;
	private final MovePlayer movePlayer;
	private final BoardDbService boardDbService;

	@PostMapping("/game")
	public Game startGame(@RequestParam String firstPlayerName, @RequestParam String secondPlayerName, @RequestParam boolean areColoursRandom) {
		Game game = gameCreator.createGame(firstPlayerName, secondPlayerName, areColoursRandom);
		return gameCreator.commitGame(game);
	}

	@GetMapping("/game")
	public Game getGame(@RequestParam int id) {
		return gameService.getGameWithMoves(id);
	}

	@GetMapping("/board")
	public String getBoard(@RequestParam int gameId) {
		return boardDbService.getBoard(gameId).getGraphicalRepresentation();
	}
	@PostMapping("/move")
	public String playMove(@RequestParam int id, @RequestParam PieceColour colour, @RequestParam String move) {
		movePlayer.playMove(id, colour, move);
		return getBoard(id);
	}
}
