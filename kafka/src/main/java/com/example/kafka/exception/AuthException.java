package com.example.kafka.exception;

import lombok.*;

import java.io.Serial;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString(callSuper = true)
public class AuthException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8496653682576808537L;

    public AuthException(String errorCode, String errorDesc) {
        super(errorCode, errorDesc);
    }
}
