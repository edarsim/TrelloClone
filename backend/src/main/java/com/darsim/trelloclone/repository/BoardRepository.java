package com.darsim.trelloclone.repository;

import com.darsim.trelloclone.dao.BoardDAO;
import com.darsim.trelloclone.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardRepository implements BoardDAO {

/*    ResultSetExtractor<List<Board>> mapper = rs -> {
        var board = new Board();
        board.setId(rs.getLong("id"));
        board.setName(rs.getString("name"));
        board.setUserId(rs.getLong("user_id"));
        return List.of(board);
    };*/

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Long createBoard(Board board) {
        var query = "INSERT INTO boards (name, user_id) VALUES (:name, :user_id) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        params.put("name", board.getName());
        params.put("userId", board.getUserId());
        return jdbcTemplate.queryForObject(query, params, Long.class);
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
        // first delete all the cards and lists that belong to the board
        var listsQuery = "DELETE FROM lists WHERE board_id = :boardId";
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        jdbcTemplate.update(listsQuery, params);

        // after that, delete the board
        var query = "DELETE FROM boards where id = :boardId";
        jdbcTemplate.update(listsQuery, params);
    }
}
