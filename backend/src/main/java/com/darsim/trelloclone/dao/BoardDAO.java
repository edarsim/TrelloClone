package com.darsim.trelloclone.dao;

import com.darsim.trelloclone.model.Board;

import java.util.List;

public interface BoardDAO {

    Long createBoard(Board board);
    List<Board> findBoardsByUserId(Long userId);
    void deleteBoard(Long boardId);
}
