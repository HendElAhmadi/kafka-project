package com.example.kafka.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2475183716837724034L;
    private String token;
    private String name;
    private String role;
}
