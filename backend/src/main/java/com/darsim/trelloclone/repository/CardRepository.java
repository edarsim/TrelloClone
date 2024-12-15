package com.darsim.trelloclone.repository;

import com.darsim.trelloclone.dao.CardDAO;
import com.darsim.trelloclone.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CardRepository implements CardDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Long addCard(Card card) {
        var query = "INSERT INTO cards (title, description, list_id) VALUES (:title, :description, :listId) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        params.put("title", card.getTitle());
        params.put("description", card.getDescription());
        params.put("listId", card.getListId());
        return jdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    public List<Card> findCardsByCardListId(Long listId) {
        var query = "SELECT * FROM cards WHERE list_id = :listId";
        Map<String, Object> params = new HashMap<>();
        params.put("listId", listId);
        return jdbcTemplate.query(query, params, (rs, rowNum) -> {
            var card = new Card();
            card.setId(rs.getLong("id"));
            card.setTitle(rs.getString("title"));
            card.setDescription(rs.getString("description"));
            card.setListId(rs.getLong("list_id"));
            return card;
        });
    }

    @Override
    public void deleteCard(Long cardId) {
        var query = "DELETE FROM cards WHERE id = :cardId";
        Map<String, Object> params = new HashMap<>();
        params.put("id", cardId);
        jdbcTemplate.update(query, params);
    }
}
