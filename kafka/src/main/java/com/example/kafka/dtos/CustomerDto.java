package com.example.kafka.dtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7055013802399252560L;

    private String nationalId;
    private String name;
    private Double amount;
}
