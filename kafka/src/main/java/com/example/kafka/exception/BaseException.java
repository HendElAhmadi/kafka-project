package com.example.kafka.exception;

import lombok.*;

import java.io.Serial;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class BaseException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 1708330077023298699L;
    private String errorCode;
    private String errorDesc;
}
