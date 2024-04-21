package com.example.kafka.web.controller;


import com.example.kafka.service.AuthenticationService;
import com.example.kafka.web.request.LoginRequest;
import com.example.kafka.web.response.LoginResponse;
import com.example.kafka.web.response.ResponseModel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                              HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeyException {

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        ResponseModel<LoginResponse> responseModel = ResponseModel.<LoginResponse>builder()
                .status(HttpStatus.OK.value()).data(loginResponse).build();
        return ResponseEntity.status(responseModel.getStatus()).body(responseModel);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseModel<Boolean>> logout() {
        boolean result = authenticationService.logout();
        ResponseModel<Boolean> response = ResponseModel.<Boolean>builder().status(HttpStatus.OK.value()).data(result).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
