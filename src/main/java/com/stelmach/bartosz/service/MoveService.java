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
    private BoardDbService boardDbService;
    @Autowired
    private MoveRepository moveRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameFlagsRepository gameFlagsRepository;

    @Transactional
    public void commitMove(Game game, Move move, GameFlags gameFlags, BoardDbWrapper boardDbWrapper, Board board){
        moveRepository.save(move);
        gameRepository.save(game);
        gameFlagsRepository.save(gameFlags);
        boardDbService.updateBoard(boardDbWrapper, board);
    }
}
