package com.stelmach.bartosz.piece;

import com.stelmach.bartosz.entity.Coordinates;
import com.stelmach.bartosz.entity.piece.Pawn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceColour.BLACK;
import static com.stelmach.bartosz.entity.Board.PieceColour.WHITE;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PawnTest {
    private static Stream<Arguments> provideCoordinatesForTestV2() {
        return Stream.of(
                Arguments.of(new Pawn(1, 1, WHITE), new Coordinates(2,1)),
                Arguments.of(new Pawn(1, 3, WHITE), new Coordinates(2,3)),
                Arguments.of(new Pawn(1, 6, WHITE), new Coordinates(2,6)),
                Arguments.of(new Pawn(2, 2, WHITE), new Coordinates(3,2)),
                Arguments.of(new Pawn(2, 7, WHITE), new Coordinates(3,7)),
                Arguments.of(new Pawn(3, 0, WHITE), new Coordinates(4,0)),
                Arguments.of(new Pawn(3, 1, WHITE), new Coordinates(4,1)),
                Arguments.of(new Pawn(4, 4, WHITE), new Coordinates(5,4)),
                Arguments.of(new Pawn(4, 6, WHITE), new Coordinates(5,6)),
                Arguments.of(new Pawn(5, 2, WHITE), new Coordinates(6,2)),
                Arguments.of(new Pawn(5, 0, WHITE), new Coordinates(6,0)),
                Arguments.of(new Pawn(1, 1, WHITE), new Coordinates(2,1)),
                Arguments.of(new Pawn(6, 3, BLACK), new Coordinates(5,3)),
                Arguments.of(new Pawn(6, 6, BLACK), new Coordinates(5,6)),
                Arguments.of(new Pawn(2, 2, BLACK), new Coordinates(1,2)),
                Arguments.of(new Pawn(2, 7, BLACK), new Coordinates(1,7)),
                Arguments.of(new Pawn(3, 0, BLACK), new Coordinates(2,0)),
                Arguments.of(new Pawn(3, 1, BLACK), new Coordinates(2,1)),
                Arguments.of(new Pawn(4, 4, BLACK), new Coordinates(3,4)),
                Arguments.of(new Pawn(4, 6, BLACK), new Coordinates(3,6)),
                Arguments.of(new Pawn(5, 2, BLACK), new Coordinates(4,2)),
                Arguments.of(new Pawn(5, 0, BLACK), new Coordinates(4,0))
        );
    }

    private static List<Arguments> provideCoordinatesForTestV1() {
        List<Arguments> args = new ArrayList<>();
        add(args, WHITE, 1);
        add(args, BLACK, -1);
        return args;
    }

    private static void add(List<Arguments> args, PieceColour colour, int rankChange) {
        for (int rank = 1; rank <= 6; rank++) {
            for (int file = 0; file <= 7; file++) {
                args.add(Arguments.of(new Pawn(rank, file, colour), new Coordinates(rank + rankChange, file)));
            }
        }
    }

    @ParameterizedTest
    @MethodSource({"provideCoordinatesForTestV1", "provideCoordinatesForTestV2"})
    void canNormalMove(Pawn pawn, Coordinates endCoordinates) {
        assertTrue(pawn.canNormalMove(endCoordinates));
    }
}