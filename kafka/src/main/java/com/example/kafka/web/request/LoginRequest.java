package com.example.kafka.web.request;


import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6398120476098980619L;
    @Pattern(regexp = "[a-zA-Z]+", message="username should contain letters only")
    @NotNull(message="username mustn't be empty")
    @NotEmpty(message="username mustn't be empty")
    private String username;

    @NotNull(message="password mustn't be empty")
    @NotEmpty(message="password mustn't be empty")
    private String password;

}
