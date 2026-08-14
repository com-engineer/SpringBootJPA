package com.example.SpringBootJWT.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//generating jwt token to assign the user after login
@Service
public class JwtService {
//    private final String SECRET = "iuherhhwhlkuroiu";//we could do it like this way hard coded
    @Value("${jwt.secret}")
    private  String jwtSecret;//the secret key are taken from the .property file
//    we can use it to sign in
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000+60+15))
                .signWith(getKey())
                .compact();
    }

    public Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())//first verify the token using secret key before parsing it
                .build()
                .parseSignedClaims(token)
                .getPayload();//"payload/claims is the main thing which contain the userdetails"
    }
}
