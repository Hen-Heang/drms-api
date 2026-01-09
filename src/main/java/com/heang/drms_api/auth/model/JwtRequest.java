package com.heang.drms_api.auth.model;

import java.io.Serializable;

public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;

    private String password;


    public JwtRequest(String username, String password) {
        this.setEmail(username);
        this.setPassword(password);
    }
    //    setter
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    Getter
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
