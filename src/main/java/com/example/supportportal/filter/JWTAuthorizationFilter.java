package com.example.supportportal.filter;

import static com.example.supportportal.constant.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.example.supportportal.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@AllArgsConstructor @NoArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){//check if request is option
            response.setStatus(HttpStatus.OK.value());//if it is we set response to OK
        }else{//otherwise
            String authorizationHeader=request.getHeader(AUTHORIZATION); //grab the header
            if(authorizationHeader==null|| authorizationHeader.startsWith(TOKEN_PREFIX)){//we make sure the authorization is not null or starts with the prefix
                //because if it does we dont care abt it and we don't work with it, only care about the one that starts with BEARER
                filterChain.doFilter(request,response); //let it low
                return;// not return anything
            }
        }
    }
}
