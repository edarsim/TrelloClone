package com.darsim.trelloclone.dao;

import com.darsim.trelloclone.model.User;

public interface UserDAO {
    Long register(User user);
    User findUserById(Long id);
    User findByUsername(String username);
}
