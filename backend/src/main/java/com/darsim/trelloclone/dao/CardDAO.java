package com.darsim.trelloclone.dao;

import com.darsim.trelloclone.model.Card;

import java.util.List;

public interface CardDAO {

    Long addCard(Card card);
    List<Card> findCardsByCardListId(Long cardListId);
    void deleteCard(Long cardId);
}
