package com.stelmach.bartosz.repository;

import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.GameFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameFlagsRepository extends JpaRepository<GameFlags, Integer> {
    Optional<GameFlags> findByGame(Game game);
}
