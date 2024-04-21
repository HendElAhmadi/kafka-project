package com.example.kafka.dtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResultWrapper implements Serializable {
    @Serial
    private static final long serialVersionUID = -8448120146945951492L;

    private List<CustomerDto> microCustomers;
    private List<CustomerDto> largeCustomers;
    private String wrongProcessedFileName;
}
