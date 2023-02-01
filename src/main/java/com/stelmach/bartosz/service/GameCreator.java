package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.Board;
import com.stelmach.bartosz.entity.BoardDbWrapper;
import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.GameFlags;
import com.stelmach.bartosz.repository.GameFlagsRepository;
import com.stelmach.bartosz.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service @Slf4j
public class GameCreator {
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private GameFlagsRepository gameFlagsRepository;
	@Autowired
	private BoardDbService boardDbService;

	public Game createGame(String firstPlayerName, String secondPlayerName, boolean areColoursRandom) {
		List<String> namesList = convertNamesToList(firstPlayerName, secondPlayerName, areColoursRandom);
		return new Game(namesList.get(0), namesList.get(1));
	}

	private List<String> convertNamesToList(String name1, String name2, boolean shuffleList) {
		List<String> namesList = new ArrayList<>() {{
			add(name1);
			add(name2);
		}};
		if(shuffleList) Collections.shuffle(namesList);

		return namesList;
	}

	@Transactional
	public Game commitGame(Game game) {
		log.trace("Persisting game: " + game);
		Game savedGame = gameRepository.save(game);
		GameFlags gameFlags = new GameFlags(savedGame);
		gameFlags = gameFlagsRepository.save(gameFlags);
		BoardDbWrapper boardDbWrapper = boardDbService.saveBoard(game, new Board());
		log.trace("Persisted game: " + savedGame + ", gameFlags: " + gameFlags + ", board: " + boardDbWrapper);
		return savedGame;
	}
}
