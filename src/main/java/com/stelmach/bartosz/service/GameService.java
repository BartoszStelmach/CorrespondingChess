package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.Move;
import com.stelmach.bartosz.repository.GameRepository;
import com.stelmach.bartosz.repository.MoveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class GameService {
	@Autowired
	public GameService(GameRepository gameRepository, MoveRepository moveRepository) {
		this.gameRepository = gameRepository;
		this.moveRepository = moveRepository;
	}

	private final GameRepository gameRepository;
	private final MoveRepository moveRepository;

	public Game getGame(int id) {
		return gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found for the id: " + id));
	}

	public Game getGameWithMoves(int id) {
		Game game = getGame(id);
		List<Move> moveList = moveRepository.findByGame(game);
		List<String> moves = convertMovesToStrings(moveList);
		game.setMoves(moves);
		return game;
	}

	private List<String> convertMovesToStrings(List<Move> moveList) {
		List<String> resultStrings = new ArrayList<>();
		for (int i=0; i<moveList.size(); i=i+2) {
			String pairOfMoves = String.valueOf(i/2 + 1);
			pairOfMoves += ". ";
			pairOfMoves += moveList.get(i).getNotation();
			if (moveList.size() > i+1)	{
				pairOfMoves += " ";
				pairOfMoves += moveList.get(i+1).getNotation();
			}
			resultStrings.add(pairOfMoves);
		}
		return resultStrings;
	}
}
