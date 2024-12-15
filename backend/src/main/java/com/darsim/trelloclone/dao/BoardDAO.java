package com.darsim.trelloclone.dao;

import com.darsim.trelloclone.model.Board;

import java.util.List;

public interface BoardDAO {

    Long createBoard(String boardName, String username);
    List<Board> getAllBoards();
    List<Board> findBoardsByUserId(Long userId);
    void deleteBoard(Long boardId);
}
