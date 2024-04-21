package com.example.kafka.exception;

import lombok.*;

import java.io.Serial;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString(callSuper = true)
public class BadRequestException extends BaseException{

    @Serial
    private static final long serialVersionUID = -1737770024387151105L;

    public BadRequestException(String errorCode, String errorDesc) {
        super(errorCode, errorDesc);
    }
}
