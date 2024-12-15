package com.darsim.trelloclone.model;

import lombok.Data;

@Data
public class Card {

    private Long id;
    private String text;
    private Long cardListId;
}
