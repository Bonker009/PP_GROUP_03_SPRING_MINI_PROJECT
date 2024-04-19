package org.example.miniprojectspring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.entity.CustomUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; //5 hour
    public static final String SECRET = "3445f76b8b7c0ff73d2f38a33a6c3b9b59b9d5e0ff9460b9935ab56dbc888c64";

    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }


    private Key getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        System.out.println(userDetails + " UserDetail");
        String userIdFromUserDetail = String.valueOf(getUserIdFromUserDetails(userDetails));
        String profileImageFromUserDetail = getProfileImageFromUserDetails(userDetails);

        System.out.println(userIdFromUserDetail + "UserDetail Customer");
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userIdFromUserDetail);
        claims.put("profileImage", profileImageFromUserDetail);


        return createToken(claims, userDetails);
    }

    private UUID getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetail customUserDetail) {
            AppUserDTO appUserDTO = customUserDetail.getAppUserDTO();
            if (appUserDTO != null) {
                return appUserDTO.getUserId();
            }
        }
        return null;
    }
    private String getProfileImageFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetail customUserDetail) {
            AppUserDTO appUserDTO = customUserDetail.getAppUserDTO();
            if (appUserDTO != null) {
                return appUserDTO.getProfileImage();
            }
        }
        return null;
    }


    private Claims extractAllClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}