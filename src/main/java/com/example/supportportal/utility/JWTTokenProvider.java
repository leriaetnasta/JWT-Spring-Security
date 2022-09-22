package com.example.supportportal.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import static com.example.supportportal.constant.SecurityConstant.*;
import static java.util.Arrays.stream;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.supportportal.domaine.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

//generate the token
    public  String generateJwtToken(UserPrincipal userPrincipal){ //getting user after user've been authentication
        String[] claims=getClaimFromUser(userPrincipal);
        return JWT.create().withIssuer(GET_ARRAYS_LLC) //our company name
                .withAudience(GET_ARRAYS_ADMINISTRATION)//ADMIN PPL
                .withIssuedAt(new Date())//WHEN ITS ISSUED
                .withSubject(userPrincipal.getUsername())//NAME OF USER
                .withArrayClaim(AUTHORITIES,claims)//CLAIM OF USER
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));

    }
    //to get the authorities
    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims= getClaimFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(username,null,authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }
    //check if token is there
    public boolean isTokenValid(String username, String token){
        JWTVerifier jwtVerifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExprired(jwtVerifier,token);
    }
    public String getSubject(String token){
        JWTVerifier jwtVerifier= getJWTVerifier();
        return jwtVerifier.verify(token).getSubject();
    }
    private boolean isTokenExprired(JWTVerifier jwtVerifier, String token) {
        Date date=jwtVerifier.verify(token).getExpiresAt();
        return date.before(new Date());
    }

    private String[] getClaimFromToken(String token) {
        JWTVerifier jwtVerifier=getJWTVerifier();
        return jwtVerifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() { //to verify the token
        JWTVerifier jwtVerifier;
        try{
            Algorithm algorithm=Algorithm.HMAC512(secret);
            jwtVerifier=JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        }catch (JWTVerificationException e){
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return jwtVerifier;
    }

    private String[] getClaimFromUser(UserPrincipal userPrincipal) { //generate the token
        List<String> authorities=new ArrayList<>();
        for(GrantedAuthority grantedAuthority: userPrincipal.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]); // return as an array of strings
    }
}
