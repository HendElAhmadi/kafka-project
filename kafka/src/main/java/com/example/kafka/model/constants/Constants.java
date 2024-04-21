package com.example.kafka.model.constants;

public final class Constants {
    private Constants(){}
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String SUBJECT = "LIBRARY";
    public static final String MOBILE_REGEX = "^(?:\\+971|00971|0)(5[0-6]|2|3|4|6|7|9|5[0-2568])\\d{7}$";
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
    public static final String EGY_NID_REGEX ="^[0-9]{14}$";
    public static final String LETTERS_ONLY_REGEX ="^[a-zA-Z]+$";
    public static final String NUMBERS_ONLY_REGEX ="^[0-9]+$";
}
