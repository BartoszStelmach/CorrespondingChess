package com.stelmach.bartosz.game.repository;

import com.stelmach.bartosz.game.component.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

}
