package com.example.SpringBootJWT.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor//Creates a constructor only for final (and @NonNull) fields.
public class JwtAuthFilter extends OncePerRequestFilter {//it filter the request exactly once
    private final JwtService jwtService;//to remove the error we use the "RequiredArgsConstructor" annotation
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            //here what we want is not present then there is no point of validating the token
            //we will just pass the filters rest filters will invastigate
            filterChain.doFilter(request,response);
            return;
        }

        //if token is present it would throw exception or error so we will wrap it in the try catch block
        try{
            //take the token part by removing "Bearer " part
            String  token = header.substring(7);
//            parse the token using the method written in the jwtService
            Claims claims = jwtService.parseToken(token);
            //after successful varification now we need to store the user in the SecurityContextHolder
            //for the context for that we need the user details
            String email = claims.getSubject();//return the email
            UserDetails user = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (Exception exception){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        filterChain.doFilter(request,response);

    }

}
