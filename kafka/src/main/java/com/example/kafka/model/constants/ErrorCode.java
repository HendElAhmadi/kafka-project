package com.example.kafka.model.constants;

public enum ErrorCode {
    ENTITY_NOT_FOUND("Entity is not found!"),
    ACCESS_IS_NOT_ALLOWED("Access is not Allowed"),
    SOMETHING_WENT_WRONG("Something went wrong"),
    TOKEN_IS_NEEDED("Token is required"),
    INVALID_TOKEN("Token is invalid"),
    EXPIRED_TOKEN("Token is expired"),
    INVALID_CREDENTIALS("Credentials are not valid"),
    NID_MUST_NOT_BE_NULL("Egyptian national id must not be empty"),
    NID_NOT_VALID("Egyptian national is not valid"),
    NAME_MUST_NOT_BE_NULL("Name must not be empty"),
    NAME_NOT_VALID("Name is not valid");

    private String errorDesc;

    ErrorCode(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
