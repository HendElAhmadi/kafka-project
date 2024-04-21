package com.example.kafka.service;

import com.example.kafka.exception.AuthException;
import com.example.kafka.model.constants.ErrorCode;
import com.example.kafka.web.request.LoginRequest;
import com.example.kafka.web.response.LoginResponse;
import com.example.kafka.web.security.CustomAuthentication;
import com.example.kafka.web.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private TokenManager tokenManager;

    @Value("${credentials.username}")
    private String username;

    @Value("${credentials.password}")
    private String password;

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        if(!loginRequest.getUsername().equals(username) && !loginRequest.getPassword().equals(password))
            throw new AuthException(ErrorCode.INVALID_CREDENTIALS.name(),ErrorCode.INVALID_CREDENTIALS.getErrorDesc());

        final List<String> token = tokenManager.generateToken(loginRequest.getUsername(),
                Collections.singletonList("ADMIN"));
        Collection<GrantedAuthority> authorities = tokenManager.getRolesFromToken(token.get(0))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(token.get(0), authorities));


        return LoginResponse.builder().token(token.get(0))
                .name(loginRequest.getUsername())
                .role("ADMIN").build();
    }

    @Override
    public Boolean logout() {
        return true;
    }
}
