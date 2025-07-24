package com.kenny.uaa.config;

public class Constants {
    public static final String PROBLEM_BASE_URI = "http://localhost:8080";
    /*
     * - Need to update PATTERN_MOBILE to match Hong Kong mobile numbers:

        - Starts with 5, 6, or 9
        - 8 digits total
        - Optional +852 country code

        - New regex should validate:

        - Numbers like 51234567, 61234567, 91234567
        - Or +85251234567

        - Should maintain the existing PROBLEM_BASE_URI constant

     */
    public static final String PATTERN_MOBILE = "^(\\+852)?[569]\\d{7}$";
    public static final String ROLE_USER = "ROLE_USER";
}
