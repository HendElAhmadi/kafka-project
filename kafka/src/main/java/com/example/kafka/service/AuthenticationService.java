package com.example.kafka.service;


import com.example.kafka.web.request.LoginRequest;
import com.example.kafka.web.response.LoginResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeyException;
    Boolean logout();
}
