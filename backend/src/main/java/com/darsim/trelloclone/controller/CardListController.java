package com.darsim.trelloclone.controller;

import com.darsim.trelloclone.dao.CardListDAO;
import com.darsim.trelloclone.model.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards/{boardId}/lists")
public class CardListController {

    @Autowired
    private CardListDAO cardListDAO;

    @PostMapping
    public Long createCardList(@PathVariable Long boardId, @RequestBody Map<String, String> requestBody){
        return cardListDAO.createCardList(requestBody.get("name"), boardId);
    }

    @GetMapping
    public List<CardList> getCardListByBoardId(@PathVariable Long boardId){
        return cardListDAO.findCardListByBoardId(boardId);
    }

    @DeleteMapping("/{listId}")
    public void deleteCardList(@PathVariable Long listId){
        cardListDAO.deleteCardList(listId);
    }
}
