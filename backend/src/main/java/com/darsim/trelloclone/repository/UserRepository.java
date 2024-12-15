package com.darsim.trelloclone.repository;

import com.darsim.trelloclone.dao.UserDAO;
import com.darsim.trelloclone.model.User;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository implements UserDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long register(User user) {
        var query = "INSERT INTO users (username, password, email) VALUES ( :username, :password, :email) RETURNING id";
        Map<String, Object> params = new HashMap<>();
        var hashedPassword = passwordEncoder.encode(user.getPassword());
        params.put("username", user.getUsername());
        params.put("password", hashedPassword);
        params.put("email", user.getEmail());
        return jdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    public User findUserById(Long id) { // would be better to use Optional<User>...
        var query = "SELECT * FROM users WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(query, params, (rs, rowNum) -> User.builder()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .build());
    }

    @Override
    public User findByUsername(String username) {
        var query = "SELECT * FROM users WHERE username = :username";
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        return jdbcTemplate.queryForObject(query, params, (rs, rowNum) -> User.builder()
         .id(rs.getLong("id"))
         .username(rs.getString("username"))
         .password(rs.getString("password"))
         .email(rs.getString("email"))
         .build());
    }
}
