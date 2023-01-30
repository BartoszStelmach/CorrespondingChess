package com.stelmach.bartosz.board.service;

import com.stelmach.bartosz.board.component.Board;
import com.stelmach.bartosz.piece.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stelmach.bartosz.board.component.Board.PieceColour;
import static com.stelmach.bartosz.board.component.Board.PieceColour.BLACK;
import static com.stelmach.bartosz.board.component.Board.PieceColour.WHITE;
import static com.stelmach.bartosz.board.component.Board.PieceType;
import static com.stelmach.bartosz.board.component.Board.PieceType.*;

@Service
public class BoardInitializer {
	public static void initialize(Board board){
		Piece[][] pieces = board.getPieces();
		Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces = board.getGroupedPieces();
		initializePieceGroupings(groupedPieces);
		initializeWhiteNonPawnPieces(groupedPieces, pieces);
		initializeWhitePawns(groupedPieces, pieces);
		initializeEmptySquares(pieces);
		initializeBlackPawns(groupedPieces, pieces);
		initializeBlackNonPawnPieces(groupedPieces, pieces);
	}

	private static void initializePieceGroupings(Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces) {
		initializePieceGroupingsForColour(WHITE, groupedPieces);
		initializePieceGroupingsForColour(BLACK, groupedPieces);
	}

	private static void initializePieceGroupingsForColour(PieceColour colour, Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces) {
        groupedPieces.put(colour, new HashMap<>() {{
			put(PAWN, new ArrayList<>());
			put(ROOK, new ArrayList<>());
			put(KNIGHT, new ArrayList<>());
			put(BISHOP, new ArrayList<>());
			put(QUEEN, new ArrayList<>());
			put(KING, new ArrayList<>());
		}});
	}

	private static void initializeWhiteNonPawnPieces(Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces){initializeNonPawnPieces(WHITE, groupedPieces, pieces);}
	private static void initializeBlackNonPawnPieces(Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces){initializeNonPawnPieces(BLACK, groupedPieces, pieces);}

    private static void initializeNonPawnPieces(PieceColour colour, Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces) {
        int rank = colour == WHITE ? 0 : 7;

        pieces[rank][0] = new Rook(rank, 0, colour);
        pieces[rank][1] = new Knight(rank, 1, colour);
        pieces[rank][2] = new Bishop(rank, 2, colour);
        pieces[rank][3] = new Queen(rank, 3, colour);
        pieces[rank][4] = new King(rank, 4, colour);
        pieces[rank][5] = new Bishop(rank, 5, colour);
        pieces[rank][6] = new Knight(rank, 6, colour);
        pieces[rank][7] = new Rook(rank, 7, colour);

        groupedPieces.get(colour).get(ROOK).add(pieces[rank][0]);
        groupedPieces.get(colour).get(ROOK).add(pieces[rank][7]);
        groupedPieces.get(colour).get(KNIGHT).add(pieces[rank][1]);
        groupedPieces.get(colour).get(KNIGHT).add(pieces[rank][6]);
        groupedPieces.get(colour).get(BISHOP).add(pieces[rank][2]);
        groupedPieces.get(colour).get(BISHOP).add(pieces[rank][5]);
        groupedPieces.get(colour).get(QUEEN).add(pieces[rank][3]);
        groupedPieces.get(colour).get(KING).add(pieces[rank][4]);
    }

	private static void initializeWhitePawns(Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces){initializePawns(WHITE, groupedPieces, pieces);}
	private static void initializeBlackPawns(Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces){initializePawns(BLACK, groupedPieces, pieces);}
	private static void initializePawns(PieceColour colour, Map<PieceColour, Map<PieceType, List<Piece>>> groupedPieces, Piece[][] pieces) {
		int rank = colour == WHITE ? 1 : 6;

		for (int file = 0; file <= 7; file++) {
			pieces[rank][file] = new Pawn(rank, file, colour);
			groupedPieces.get(colour).get(PAWN).add(pieces[rank][file]);
		}
	}

	private static void initializeEmptySquares(Piece[][] pieces) {
		for (int rank = 2; rank <= 5; rank++) {
			for (int file = 0; file <= 7; file++) {
				pieces[rank][file] = null;
			}
		}
	}
}
