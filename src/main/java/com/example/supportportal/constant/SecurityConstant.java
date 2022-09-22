package com.example.supportportal.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME=432_000_000;// in seconds its 5 days
    public static final String TOKEN_PREFIX="Bearer ";//ownership
    public static final String JWT_TOKEN_HEADER="Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED="Token cannot be verified";
    public static final String GET_ARRAYS_LLC="Get arrays, LLC";
    public static final String GET_ARRAYS_ADMINISTRATION="User Management Portal";
    public static final String AUTHORITIES="authorities";
    public static final String FORBIDDEN_MESSAGE="You need to log in to access this page";
    public static final String ACCES_DENIED_MESSAGE="You do not have the permission to access this page";
    public static final String OPTIONS_HTTP_METHOD="OPTIONS";
    public static final String[] PUBLIC_URLS={"/user/login","/user/register","/user/resetpassword/**","/user/image/**"};

}
