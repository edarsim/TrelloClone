package com.darsim.trelloclone.repository;

import com.darsim.trelloclone.dao.BoardDAO;
import com.darsim.trelloclone.dao.UserDAO;
import com.darsim.trelloclone.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardRepository implements BoardDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UserDAO userDAO;

    @Override
    public Long createBoard(String boardName, String username) {
        var query = "INSERT INTO boards (name, user_id) VALUES (:name, :userId) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        var user = userDAO.findByUsername(username);

        params.put("name", boardName);
        params.put("userId", user.getId());
        return jdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    public List<Board> getAllBoards() {
        var query = "SELECT * FROM boards";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Board board = new Board();
            board.setId(rs.getLong("id"));
            board.setName(rs.getString("name"));
            board.setUserId(rs.getLong("user_id"));
            return board;
        });
    }

    @Override
    public List<Board> findBoardsByUserId(Long userId) {
        var query = "SELECT * FROM boards WHERE user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return jdbcTemplate.query(query, params, (rs, rowNum) -> {
            Board board = new Board();
            board.setId(rs.getLong("id"));
            board.setName(rs.getString("name"));
            board.setUserId(rs.getLong("user_id"));
            return board;
        });
    }

    @Override
    public void deleteBoard(Long boardId) {
        //TODO: first delete all the cards from all the lists on the board
        //
        // delete lists that belong to the board
        var listsQuery = "DELETE FROM card_lists WHERE board_id = :boardId";
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        jdbcTemplate.update(listsQuery, params);

        // after that, delete the board
        var boardsQuery = "DELETE FROM boards where id = :boardId";
        jdbcTemplate.update(boardsQuery, params);
    }
}
