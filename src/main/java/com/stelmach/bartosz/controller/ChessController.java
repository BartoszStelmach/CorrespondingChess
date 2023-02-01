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
	private GameCreator gameCreator;
	@Autowired
	private GameService gameService;
	@Autowired
	private MovePlayer movePlayer;
	@Autowired
	private BoardDbService boardDbService;

	@PostMapping("/startGame")
	public Game startGame(@RequestParam String firstPlayerName, @RequestParam String secondPlayerName, @RequestParam boolean areColoursRandom) {
		Game game = gameCreator.createGame(firstPlayerName, secondPlayerName, areColoursRandom);
		return gameCreator.commitGame(game);
	}

	@GetMapping("/getGame")
	public Game getGame(@RequestParam int id) {
		return gameService.getGameWithMoves(id);
	}

	@GetMapping("/getBoard")
	public String getBoard(@RequestParam int gameId) {
		return boardDbService.getBoard(gameId).getGraphicalRepresentation();
	}
	@PostMapping("/playMove")
	public String playMove(@RequestParam int id, @RequestParam PieceColour colour, @RequestParam String move) {
		movePlayer.playMove(id, colour, move);
		return getBoard(id);
	}
}
