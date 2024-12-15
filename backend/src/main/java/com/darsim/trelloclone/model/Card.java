package com.darsim.trelloclone.model;

import lombok.Data;

@Data
public class Card {

    private Long id;
    private String title;
    private String description;
    private Long listId;
}
