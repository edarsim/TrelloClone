package com.darsim.trelloclone.model;


import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;

@Data
public class User {

    @Id @With
    private Long id;
    private String username;
    private String password;
    private String email;
}
