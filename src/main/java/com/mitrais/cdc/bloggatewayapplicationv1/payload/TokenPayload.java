package com.mitrais.cdc.bloggatewayapplicationv1.payload;

public class TokenPayload {

    String username;
    String token;

    public TokenPayload(String username, String token){
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
