package com.stelmach.bartosz.repository;

import com.stelmach.bartosz.entity.BoardDbWrapper;
import com.stelmach.bartosz.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDbWrapperRepository extends JpaRepository<BoardDbWrapper, Integer> {
    BoardDbWrapper findByGame(Game game);
}
