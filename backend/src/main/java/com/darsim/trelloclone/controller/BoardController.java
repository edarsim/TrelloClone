package com.darsim.trelloclone.controller;

import com.darsim.trelloclone.dao.BoardDAO;
import com.darsim.trelloclone.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardDAO boardDAO;

    @PostMapping
    public ResponseEntity<Long> createBoard(@RequestBody Map<String, String> requestBody){
        final var name = requestBody.get("name");
        final var username = requestBody.get("username");
        final var boardId = boardDAO.createBoard(name, username);
        return ResponseEntity.ok(boardId);
    }

    @GetMapping
    public List<Board> getAllBoards(){
        return boardDAO.getAllBoards();
    }

    @GetMapping("/user/{userId}")
    public List<Board> getBoardsByUserId(@PathVariable Long userId){
        return boardDAO.findBoardsByUserId(userId);
    }

    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId){
        boardDAO.deleteBoard(boardId);
    }

}
