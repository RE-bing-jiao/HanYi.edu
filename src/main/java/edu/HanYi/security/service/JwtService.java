package edu.HanYi.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
