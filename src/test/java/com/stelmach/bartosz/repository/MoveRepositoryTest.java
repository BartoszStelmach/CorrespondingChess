package com.stelmach.bartosz.repository;

import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.entity.Move;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MoveRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MoveRepository moveRepository;


    @Test
    void findByGame() {
        Game game = new Game("a", "b");

        Move move1 = new Move(game, "e4");
        Move move2 = new Move(game, "e5");
        Move move3 = new Move(game, "Nc3");

        game = entityManager.persistAndFlush(game);
        move1 = entityManager.persistAndFlush(move1);
        move2 = entityManager.persistAndFlush(move2);
        move3 = entityManager.persistAndFlush(move3);

        List<Move> movesList = List.of(move1, move2, move3);

        assertEquals(movesList, moveRepository.findByGame(game));
    }
}