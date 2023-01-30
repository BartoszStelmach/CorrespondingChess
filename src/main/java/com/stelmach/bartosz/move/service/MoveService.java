package com.stelmach.bartosz.move.service;

import com.stelmach.bartosz.board.component.Board;
import com.stelmach.bartosz.board.component.BoardDbWrapper;
import com.stelmach.bartosz.board.service.BoardDbService;
import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.component.GameFlags;
import com.stelmach.bartosz.game.repository.GameFlagsRepository;
import com.stelmach.bartosz.game.repository.GameRepository;
import com.stelmach.bartosz.move.component.Move;
import com.stelmach.bartosz.move.repository.MoveRepository;
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
