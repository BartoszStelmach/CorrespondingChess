package com.stelmach.bartosz.board.service;

import com.stelmach.bartosz.board.component.Board;
import com.stelmach.bartosz.board.component.BoardDbWrapper;
import com.stelmach.bartosz.board.repository.BoardDbWrapperRepository;
import com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;
import com.stelmach.bartosz.game.component.Game;
import com.stelmach.bartosz.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BoardDbService {
	@Autowired
	private BoardDbWrapperRepository boardDbWrapperRepository;
	@Autowired
	private GameService gameService;

	public Board getBoard(int gameId) {
		return getBoardDbWrapper(gameId).getBoard();
	}

	public BoardDbWrapper getBoardDbWrapper(int gameId) {
		Game game = gameService.getGame(gameId);
		return getBoardDbWrapper(game);
	}

	public BoardDbWrapper getBoardDbWrapper(Game game) {
		BoardDbWrapper boardDbWrapper = boardDbWrapperRepository.findByGame(game);
		boardDbWrapper.setBoard(deserializeBoard(boardDbWrapper.getBoardBytes()));
		return boardDbWrapper;
	}

	public void updateBoard(BoardDbWrapper boardDbWrapper, Board updatedBoard) {
		byte[] serializedBoard = serializeBoard(updatedBoard);
		boardDbWrapper.setBoardBytes(serializedBoard);
		boardDbWrapperRepository.save(boardDbWrapper);
	}

	public BoardDbWrapper saveBoard(Game game, Board board) {
		byte[] serializedBoard = serializeBoard(board);
		BoardDbWrapper boardDbWrapper = new BoardDbWrapper(game, serializedBoard);
		return boardDbWrapperRepository.save(boardDbWrapper);
	}

	private byte[] serializeBoard(Board board) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos = new ObjectOutputStream(baos))
		{
			oos.writeObject(board);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new ChessAppException("Couldn't serialize the board: " + e);
		}
	}

	private static Board deserializeBoard(byte[] serializedBoard)  {
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedBoard)))
		{
			Object o  = ois.readObject();
			return (Board)o;
		} catch (IOException | ClassNotFoundException e) {
			throw new ChessAppException("Couldn't deserialize the board: " + serializedBoard, e);
		}
	}
}

