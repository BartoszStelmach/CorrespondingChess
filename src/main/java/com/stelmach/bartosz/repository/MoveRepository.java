package com.stelmach.bartosz.repository;

import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Integer> {
	List<Move> findByGame(Game game);
}
