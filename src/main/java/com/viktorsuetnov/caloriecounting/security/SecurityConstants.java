package com.viktorsuetnov.caloriecounting.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/api/auth/**";
    public static final String JWT_SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Calories ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long JWT_EXPIRATION_TIME = 600_000;
}
