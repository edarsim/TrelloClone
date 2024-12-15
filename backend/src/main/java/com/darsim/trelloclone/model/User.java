package com.darsim.trelloclone.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class User {

    @Id
    private Long id;
    private String username;
    private String password;
    private String email;

}
