package com.stelmach.bartosz.game.repository;

import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.component.GameFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameFlagsRepository extends JpaRepository<GameFlags, Integer> {
    Optional<GameFlags> findByGame(Game game);
}
