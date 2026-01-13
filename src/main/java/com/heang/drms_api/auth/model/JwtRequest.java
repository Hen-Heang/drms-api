package com.heang.drms_api.auth.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class JwtRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //    setter
    //    Getter
    private String email;
    private String password;


    public JwtRequest(String username, String password) {
        this.setEmail(username);
        this.setPassword(password);
    }

}
