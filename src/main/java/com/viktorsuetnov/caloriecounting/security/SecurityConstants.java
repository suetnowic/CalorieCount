package com.viktorsuetnov.caloriecounting.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/api/v*/auth/**";
    public static final String JWT_SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Calories ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long JWT_EXPIRATION_TIME = 600_000;
    public static final String ACTIVATE_ACCOUNT_URL = "/api/v*/activate/*";
    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
}
