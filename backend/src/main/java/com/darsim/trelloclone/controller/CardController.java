package com.darsim.trelloclone.controller;

import com.darsim.trelloclone.dao.CardDAO;
import com.darsim.trelloclone.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardDAO cardDAO;

    @PostMapping("/lists")
    public Long createCard(@RequestBody Card card){
        return cardDAO.addCard(card);
    }

    @GetMapping("/lists/{listId}")
    public List<Card> getCardByListId(@PathVariable Long listId){
        return cardDAO.findCardsByCardListId(listId);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCard(@PathVariable Long cardId){
        cardDAO.deleteCard(cardId);
    }
}
