package com.example.kafka.exception;

import lombok.*;

import java.io.Serial;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString(callSuper = true)
public class BusinessException extends BaseException{
    @Serial
    private static final long serialVersionUID = 6678084154557104242L;

    public BusinessException(String errorCode, String errorDesc) {
        super(errorCode, errorDesc);
    }
}
