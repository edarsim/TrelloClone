package com.darsim.trelloclone.dao;

import com.darsim.trelloclone.model.CardList;

import java.util.List;

public interface CardListDAO {

    Long createCardList(String name, Long boardId);
    List<CardList> findCardListByBoardId(Long boardId);
    void deleteCardList(Long listId);
}
