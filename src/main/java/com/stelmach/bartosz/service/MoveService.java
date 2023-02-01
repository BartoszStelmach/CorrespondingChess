package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.*;
import com.stelmach.bartosz.repository.GameFlagsRepository;
import com.stelmach.bartosz.repository.GameRepository;
import com.stelmach.bartosz.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MoveService {
    @Autowired
    public MoveService(BoardDbService boardDbService, MoveRepository moveRepository, GameRepository gameRepository, GameFlagsRepository gameFlagsRepository) {
        this.boardDbService = boardDbService;
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.gameFlagsRepository = gameFlagsRepository;
    }

    private final BoardDbService boardDbService;
    private final MoveRepository moveRepository;
    private final GameRepository gameRepository;
    private final GameFlagsRepository gameFlagsRepository;

    @Transactional
    public void commitMove(Game game, Move move, GameFlags gameFlags, BoardDbWrapper boardDbWrapper, Board board){
        moveRepository.save(move);
        gameRepository.save(game);
        gameFlagsRepository.save(gameFlags);
        boardDbService.updateBoard(boardDbWrapper, board);
    }
}
