package com.stelmach.bartosz.service;

import com.stelmach.bartosz.controller.RestResponseExceptionHandler.ChessAppException;
import com.stelmach.bartosz.entity.Board;
import com.stelmach.bartosz.entity.BoardDbWrapper;
import com.stelmach.bartosz.entity.Game;
import com.stelmach.bartosz.repository.BoardDbWrapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BoardDbService {
	@Autowired
	public BoardDbService(BoardDbWrapperRepository boardDbWrapperRepository, GameService gameService) {
		this.boardDbWrapperRepository = boardDbWrapperRepository;
		this.gameService = gameService;
	}

	private final BoardDbWrapperRepository boardDbWrapperRepository;
	private final GameService gameService;

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

