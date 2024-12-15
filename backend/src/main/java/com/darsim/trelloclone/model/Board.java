package com.darsim.trelloclone.model;

import lombok.Data;

import java.util.List;

@Data
public class Board {
    private Long id;
    private String name;
    private Long userId;
    private List<CardList> boardLists; // Lists/Collections within the board
}
