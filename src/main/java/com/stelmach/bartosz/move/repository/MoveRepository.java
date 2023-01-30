package com.stelmach.bartosz.move.repository;

import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.move.component.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Integer> {
	List<Move> findByGame(Game game);
}
