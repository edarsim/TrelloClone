package com.darsim.trelloclone.model;

import lombok.Data;

import java.util.List;

@Data
public class CardList {

    private Long id;
    private String name;
    private Long boardId; // Foreign key to the Board
    private List<Card> cards; // Cards in this list
}
