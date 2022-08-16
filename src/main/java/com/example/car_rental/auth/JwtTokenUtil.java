package com.example.car_rental.auth;

import com.example.car_rental.model.User;
import com.example.car_rental.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
//@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    //@Value("${jwt.expiration}")
    private final long expirationTime = 3600000;

    //@Value("${jwt.secret}")
    private final String secret = "fsgsdgsdgsdgd424625635gdgdfgfdg!@#!$!$!$!@$dssdsd";

    private final UserService userService;

    public JwtTokenUtil(@Lazy UserService userService) {
        this.userService = userService;
    }

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        User user = userService.readUserByEmail(email);
        claims.put("Id", user.getUser_id());
        claims.put("Role", user.getRole());
        return doGenerateToken(claims, email);
    }

    public String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, String userEmail) {
        final String email = getSubjectFromToken(token);
        return (email.equals(userEmail) && !isTokenExpired(token));
    }
}
