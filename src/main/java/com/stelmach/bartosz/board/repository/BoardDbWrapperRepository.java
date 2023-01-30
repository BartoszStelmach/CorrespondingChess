package com.stelmach.bartosz.board.repository;

import com.stelmach.bartosz.board.component.BoardDbWrapper;
import com.stelmach.bartosz.game.component.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDbWrapperRepository extends JpaRepository<BoardDbWrapper, Integer> {
    BoardDbWrapper findByGame(Game game);
}
