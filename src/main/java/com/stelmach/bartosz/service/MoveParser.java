package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.Board;
import com.stelmach.bartosz.entity.Coordinates;
import com.stelmach.bartosz.entity.MoveDetails;
import com.stelmach.bartosz.entity.piece.Pawn;
import com.stelmach.bartosz.entity.piece.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.stelmach.bartosz.entity.Board.PieceColour;
import static com.stelmach.bartosz.entity.Board.PieceType;
import static com.stelmach.bartosz.entity.Board.PieceType.PAWN;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType;
import static com.stelmach.bartosz.entity.piece.Pawn.PawnMoveType;
import static com.stelmach.bartosz.entity.piece.Pawn.PawnMoveType.EN_PASSANT;
import static com.stelmach.bartosz.entity.piece.Pawn.PawnMoveType.TAKING;

@Service
public class MoveParser {
	@Autowired
	public MoveParser(MoveNotationParser moveNotationParser, BoardLegalityService boardLegalityService) {
		this.moveNotationParser = moveNotationParser;
		this.boardLegalityService = boardLegalityService;
	}

	private final MoveNotationParser moveNotationParser;
	private final BoardLegalityService boardLegalityService;

	public MoveDetails parseMove(String move, PieceColour pieceColour, Board board) {
		moveNotationParser.verifyGeneralLength(move);
		moveNotationParser.verifyAllCharacters(move);

		MoveDetails.BasicMoveDetails basicMoveDetails = parseMoveCommon(move, pieceColour);
		MoveDetails.SpecificMoveDetails specificMoveDetails = parseMoveSpecific(basicMoveDetails);

		Coordinates endCoordinates = getEndSquare(move, basicMoveDetails.getPieceType() == PAWN, basicMoveDetails.isAmbiguous(), specificMoveDetails.isTaking(), specificMoveDetails.getCastlingType());

		MoveDetails moveDetails = new MoveDetails(basicMoveDetails, specificMoveDetails, endCoordinates);

		Piece pieceToMove = getPieceToMove(moveDetails, board);
		moveDetails.setPieceToMove(pieceToMove);
		if(basicMoveDetails.getPieceType() == PAWN) moveDetails.setPawnMoveType(determinePawnMoveType((Pawn)pieceToMove, moveDetails, board));

		moveNotationParser.verifyLength(moveDetails);
		return moveDetails;
	}

	private MoveDetails.BasicMoveDetails parseMoveCommon(String move, PieceColour pieceColour) {
		PieceType pieceType = moveNotationParser.parsePieceType(move);
		boolean isMoveAmbiguous = moveNotationParser.isMoveAmbiguous(move);
		boolean isMoveCheckingExplicitly = moveNotationParser.isMoveChecking(move);
		boolean isMoveMating = moveNotationParser.isMoveMating(move);
		boolean isMoveChecking = isMoveCheckingExplicitly || isMoveMating;

		return new MoveDetails.BasicMoveDetails(pieceType, move, pieceColour, isMoveChecking, isMoveMating, isMoveAmbiguous);
	}

	private MoveDetails.SpecificMoveDetails parseMoveSpecific(MoveDetails.BasicMoveDetails basicMoveDetails) {
		return basicMoveDetails.getPieceType() == PAWN
				? parseMovePawnSpecific(basicMoveDetails)
				: parseMoveNonPawnSpecific(basicMoveDetails);
	}

	private MoveDetails.SpecificMoveDetails parseMovePawnSpecific(MoveDetails.BasicMoveDetails basicMoveDetails) {
		String move = basicMoveDetails.getNotation();
		boolean isMoveTaking = moveNotationParser.isPawnMoveTaking(move);
		boolean isMovePromoting = moveNotationParser.isMovePromoting(move, basicMoveDetails.isChecking());
		PieceType promotionPieceType = isMovePromoting ? moveNotationParser.getPromotionPieceType(move, basicMoveDetails.isChecking()) : null;
		return new MoveDetails.SpecificMoveDetails(isMoveTaking, isMovePromoting, null, promotionPieceType);
	}

	private MoveDetails.SpecificMoveDetails parseMoveNonPawnSpecific(MoveDetails.BasicMoveDetails basicMoveDetails) {
		String move = basicMoveDetails.getNotation();
		boolean isMoveTaking = moveNotationParser.isMoveTaking(move, basicMoveDetails.isAmbiguous());
		CastlingType castlingType = moveNotationParser.getCastlingType(move);
		return new MoveDetails.SpecificMoveDetails(isMoveTaking, false, castlingType, null);
	}

	public Coordinates getEndSquare(String move, boolean isPawnMove, boolean isMoveAmbiguous, boolean isMoveTaking, CastlingType castlingType) {
		if(castlingType != null) return null;
		int fileIndex = 0;

		if (!isPawnMove) fileIndex++;
		if (isMoveAmbiguous) fileIndex++;
		if (isMoveTaking) fileIndex++;
		if (isPawnMove && isMoveTaking) fileIndex++;

		int rankIndex = fileIndex + 1;

		int endSquareRank = Integer.parseInt(String.valueOf(move.charAt(rankIndex))) - 1;
		int endSquareFile = move.charAt(fileIndex) - 97;
		return new Coordinates(endSquareRank, endSquareFile);
	}

	public Piece getPieceToMove(MoveDetails moveDetails, Board board) {
		List<Piece> pieces = board.getPiecesForPieceType(moveDetails.getColour(), moveDetails.getPieceType());
		List<Piece> moveCandidates = filterPieces(pieces, p -> p.canItReachTheCoordinates(moveDetails.getEndCoordinates()));

		Piece pieceToMove = null;
		if (moveCandidates.size() == 1) pieceToMove = moveCandidates.get(0);
		if (moveCandidates.size() == 0) throw new IllegalArgumentException("There's no piece that can perform this move");
		if (moveCandidates.size() > 1) pieceToMove = resolveAmbiguity(moveDetails, moveCandidates);
		return pieceToMove;
	}

	private Piece resolveAmbiguity(MoveDetails moveDetails, List<Piece> moveCandidates) {
		if(moveDetails.getPieceType() == PAWN && !moveDetails.isTaking()) moveCandidates = filterPieces(moveCandidates, p -> p.getFile() == moveDetails.getEndCoordinates().getFile());
		else moveCandidates = filterMoveCandidates(moveCandidates, moveDetails.getNotation().charAt(1));

		if (moveCandidates.size() != 1) throw new IllegalArgumentException("Move is ambiguous.");
		else return moveCandidates.get(0);
	}

	private List<Piece> filterMoveCandidates (List<Piece> moveCandidates, char disambiguateChar) {
		String charString = String.valueOf(disambiguateChar);
		boolean isSecondCharRank = moveNotationParser.isStringCharRank(charString);
		boolean isSecondCharFile = moveNotationParser.isStringCharFile(charString);
		Predicate<Piece> predicate;

		if(isSecondCharRank)  predicate = m -> m.getRank() == Integer.parseInt(charString) - 1;
		else if(isSecondCharFile)  predicate = m -> m.getFile() == disambiguateChar - 97;
		else throw new IllegalArgumentException("Move is ambiguous.");

		return filterPieces(moveCandidates, predicate);
	}

	public List<Piece> filterPieces(List<Piece> pieces, Predicate<Piece> predicate) {
		return pieces.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}

	public PawnMoveType determinePawnMoveType(Pawn pawn, MoveDetails moveDetails, Board board) {
		PawnMoveType pawnMoveType = pawn.determineMoveType(moveDetails.getEndCoordinates(), moveDetails.isTaking());
		if (pawnMoveType == TAKING) {
			if(boardLegalityService.isPieceOnSquare(moveDetails.getEndCoordinates(), board)) pawnMoveType = TAKING;
			else if(boardLegalityService.isPieceOnSquare(moveDetails.getEnPassantTakingCoordinates(), board)) pawnMoveType = EN_PASSANT;
			else throw new IllegalArgumentException("Invalid taking pawn move.");
		}
		return pawnMoveType;
	}
}
