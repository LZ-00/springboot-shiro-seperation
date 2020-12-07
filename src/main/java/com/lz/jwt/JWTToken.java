package com.lz.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 乐。
 */
public class JWTToken implements AuthenticationToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
