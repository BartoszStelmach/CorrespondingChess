package com.stelmach.bartosz.service;

import com.stelmach.bartosz.entity.MoveDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.stelmach.bartosz.entity.Board.PieceType;
import static com.stelmach.bartosz.entity.Board.PieceType.PAWN;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType.LONG;
import static com.stelmach.bartosz.entity.MoveDetails.CastlingType.SHORT;

@Service
public class MoveNotationParser {
	public void verifyGeneralLength(String move) {
		if (move.length() < 2 || move.length() > 7) throw new IllegalArgumentException("Length of the move notation is incorrect.");
	}

	public void verifyLength(MoveDetails moveDetails) {
		int correctLength;
		if (moveDetails.getCastlingType() != null) correctLength = getCastlingNotationLength(moveDetails.getCastlingType());
		else if (moveDetails.isTaking()) correctLength = 4;
		else if (moveDetails.getPieceType() == PAWN) correctLength = 2;
		else correctLength = 3;

		if (moveDetails.isPromoting()) correctLength += 2;
		if (moveDetails.isChecking() || moveDetails.isMating()) correctLength += 1;

		if (moveDetails.getNotation().length() != correctLength) throw new IllegalArgumentException("Length of the move notation is incorrect.");
	}
	private int getCastlingNotationLength(CastlingType castlingType) {
		return castlingType == LONG ? 5 : 3;
	}

	public void verifyAllCharacters(String move) {
		String pieceRegex = "KQBNRP";
		String rankRegex = "1-8";
		String fileRegex = "a-h";
		String othersRegex = "x+#O\\-=";
		String finalRegex = "[^" + pieceRegex + rankRegex + fileRegex + othersRegex + "]";

		Pattern pattern = Pattern.compile(finalRegex);
		Matcher matcher = pattern.matcher(move);
		boolean isAnyCharacterNotAllowed = matcher.find();
		if (isAnyCharacterNotAllowed) throw new IllegalArgumentException("There are illegal characters in the move notation.");
	}

	public boolean isPawnMove(String move) {
		String firstChar = move.substring(0, 1);

		Pattern pattern = Pattern.compile("[a-h]");
		Matcher matcher = pattern.matcher(firstChar);
		return matcher.find();
	}

	public boolean isPawnMoveTaking(String move) {
		if (!move.contains("x")) return false;

		String secondChar = move.substring(1, 2);
		if (secondChar.equals("x")) return true;
		else throw new IllegalArgumentException ("Taking symbol 'x' is not the second character of the pawn taking move notation");
	}

	public boolean isMoveAmbiguous(String move) {
		if(move.length() < 4) return false;
		String firstChar = move.substring(0, 1);
		String secondChar = move.substring(1, 2);
		String thirdChar = move.substring(2, 3);
		String fourthChar = move.substring(3, 4);

		Pattern pattern = Pattern.compile("[KQBNRP]");
		Matcher matcher = pattern.matcher(firstChar);
		boolean isFirstCharPiece = matcher.find();

		pattern = Pattern.compile("[a-h1-8]");
		matcher = pattern.matcher(secondChar);
		boolean isSecondCharRankOrFile = matcher.find();

		matcher = pattern.matcher(thirdChar);
		boolean isThirdCharRankOrFile = matcher.find();

		matcher = pattern.matcher(fourthChar);
		boolean isFourthCharRankOrFile = matcher.find();

		return (isFirstCharPiece && isSecondCharRankOrFile && isThirdCharRankOrFile && isFourthCharRankOrFile);
	}

	public boolean isMoveTaking(String move, boolean isMoveAmbiguous) {
		if (!move.contains("x")) return false;

		int expectedTakingSymbolIndex = isMoveAmbiguous ? 2 : 1;
		if (move.charAt(expectedTakingSymbolIndex) == 'x') return true;
		else throw new IllegalArgumentException ("Taking symbol 'x' is not in the expected place of the non-pawn taking move notation");
	}

	public boolean isMoveChecking(String move) {
		if (!move.contains("+")) return false;

		int moveLength = move.length();
		String lastChar = move.substring(moveLength - 1, moveLength);

		if (lastChar.equals("+")) return true;
		else throw new IllegalArgumentException ("Checking symbol '+' is not the last character of the move notation");
	}

	public boolean isMoveMating(String move) {
		if (!move.contains("#")) return false;

		int moveLength = move.length();
		String lastChar = move.substring(moveLength - 1, moveLength);

		if (lastChar.equals("#")) return true;
		else throw new IllegalArgumentException ("Mating symbol '#' is not the last character of the move notation");
	}

	public boolean isMovePromoting(String move, boolean isMoveCheckingOrMating) {
		if (!move.contains("=")) return false;

		int expectedPromotionSymbolIndexFromEnd = isMoveCheckingOrMating ? 1 : 2;
		char expectedPromotionSymbol = move.charAt(move.length() - expectedPromotionSymbolIndexFromEnd);
		if (expectedPromotionSymbol == '=') return true;
		else throw new IllegalArgumentException ("Promotion symbol '=' is not in the expected place of the move notation");
	}

	public PieceType getPromotionPieceType(String move, boolean isMoveCheckingOrMating) {
		int expectedPromotionPieceTypeSymbolIndexFromEnd = isMoveCheckingOrMating ? 0 : 1;
		char expectedPromotionPieceTypeSymbol = move.charAt(move.length() - expectedPromotionPieceTypeSymbolIndexFromEnd);
		return PieceType.get(String.valueOf(expectedPromotionPieceTypeSymbol)).orElseThrow(() -> new IllegalArgumentException("Invalid promotion piece type symbol: " + expectedPromotionPieceTypeSymbol));
	}

	public CastlingType getCastlingType(String move) {
		if("O-O".equals(move) || "O-O+".equals(move) || "O-O#".equals(move)) return SHORT;
		if("O-O-0".equals(move) || "O-O-O+".equals(move) || "O-O-O#".equals(move)) return LONG;
		return null;
	}

	public PieceType parsePieceType(String move) {
		if(isPawnMove(move)) return PAWN;
		String firstChar = move.substring(0, 1);
		return PieceType.get(firstChar).orElseThrow(() -> new IllegalArgumentException("Invalid piece type symbol: " + firstChar));
	}

	public boolean isStringCharRank(String supposedRankChar) {
		return doesStringMatch("[1-8]", supposedRankChar);
	}

	public boolean isStringCharFile(String supposedFileChar) {
		return doesStringMatch("[a-h]", supposedFileChar);
	}

	public boolean doesStringMatch(String regex, String s) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}

//	public static class Symbols {
//		public static final String TAKING = "x";
//		public static final String PIECE_REGEX = "KQBNRP";
//		public static final String RANK_REGEX = "1-8";
//		public static final String FILE_REGEX = "a-h";
//
//		public static final String OTHERS_REGEX = "x+#O\\-=";
//		public static final String FINAL_REGEX = "[^" + PIECE_REGEX + RANK_REGEX + FILE_REGEX + OTHERS_REGEX + "]";
//	}
}
