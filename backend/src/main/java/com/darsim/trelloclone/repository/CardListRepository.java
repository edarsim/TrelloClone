package com.darsim.trelloclone.repository;

import com.darsim.trelloclone.dao.CardListDAO;
import com.darsim.trelloclone.model.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CardListRepository implements CardListDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Long createCardList(String name, Long boardId) {
        var query = "INSERT INTO card_lists (name, board_id) VALUES (:name, :boardId) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("boardId", boardId);
        return jdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    public List<CardList> findCardListByBoardId(Long boardId) {
        var query = "SELECT * FROM card_lists WHERE board_id = :boardId";
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        return jdbcTemplate.query(query, params, (rs, rowNum) -> {
            var cardList = new CardList();
            cardList.setId(rs.getLong("id"));
            cardList.setName(rs.getString("name"));
            cardList.setBoardId(rs.getLong("board_id"));
            return cardList;
        });
    }

    @Override
    public void deleteCardList(Long listId) {
        // first delete the cards belonging to this list
        var cardsQuery = "DELETE FROM cards WHERE list_id = :listId";
        Map<String, Object> params = new HashMap<>();
        params.put("listId", listId);
        jdbcTemplate.update(cardsQuery, params);

        // then delete the list
        var query = "DELETE FROM card_lists WHERE id = :listId";
        jdbcTemplate.update(query, params);
    }
}
