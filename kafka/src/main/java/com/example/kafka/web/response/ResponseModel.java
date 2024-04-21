package com.example.kafka.web.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    private T data;
    private int status;
    private String errorCode;
    private String errorDesc;
}
