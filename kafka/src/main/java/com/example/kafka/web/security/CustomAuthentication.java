package com.example.kafka.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class CustomAuthentication implements Authentication {

    @Serial
    private static final long serialVersionUID = 4607711880142070617L;

    private String token;
    private Collection<GrantedAuthority> grantedAuthorities;
    private boolean authenticated =true;

    public CustomAuthentication(String token, Collection<GrantedAuthority> grantedAuthorities) {
        this.token = token;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated=isAuthenticated;
    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getToken() {
        return token;
    }
}

