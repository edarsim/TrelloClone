package com.darsim.trelloclone.controller;

import com.darsim.trelloclone.dao.UserDAO;
import com.darsim.trelloclone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping("/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userDAO.findUserById(userId);
    }

    @RequestMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username){
        return userDAO.findByUsername(username);
    }
}
