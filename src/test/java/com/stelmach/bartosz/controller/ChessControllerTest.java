package com.stelmach.bartosz.controller;

import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.service.BoardDbService;
import com.stelmach.bartosz.service.GameCreator;
import com.stelmach.bartosz.service.GameService;
import com.stelmach.bartosz.service.MovePlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ChessController.class)
class ChessControllerTest {
    @MockBean
    GameCreator gameCreator;
    @MockBean
    GameService gameService;
    @MockBean
    MovePlayer movePlayer;
    @MockBean
    BoardDbService boardDbService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void idForExistingGameReturnsGame() throws Exception {
        Game game = new Game("a", "b");
        game.setID(1);
        game.setMoves(List.of("e4", "e5", "Nc3"));
        when(gameService.getGameWithMoves(1)).thenReturn(game);

        mockMvc.perform(get("/game")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.whiteName").value("a"))
                .andExpect(jsonPath("$.blackName").value("b"))
                .andExpect(jsonPath("$.moves[0]").value("e4"))
                .andExpect(jsonPath("$.moves[1]").value("e5"))
                .andExpect(jsonPath("$.moves[2]").value("Nc3"));
    }

    @Test
    void idForNonExistingGameThrowsException() throws Exception {
        int id = 1;
        when(gameService.getGameWithMoves(1)).thenThrow(new IllegalArgumentException("Game not found for the id: " + id));

        mockMvc.perform(get("/game")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Game not found for the id: " + id, result.getResolvedException().getMessage()))
                .andExpect(content().string("Cannot perform specified action. Please check if it's correct.\nCause: Game not found for the id: " + id));
    }
}