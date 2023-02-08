package com.github.yuriiyukh.stogram.security;

public class SecurityConstants {

    public static final String SIGN_UP_URLS = "/api/auth/**";
    
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTET_TYPE = "application/json";
    
}
