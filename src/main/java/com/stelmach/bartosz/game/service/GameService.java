package com.stelmach.bartosz.game.service;

import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.repository.GameRepository;
import com.stelmach.bartosz.move.component.Move;
import com.stelmach.bartosz.move.repository.MoveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class GameService {
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private MoveRepository moveRepository;

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
