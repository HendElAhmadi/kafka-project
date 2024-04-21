package com.example.kafka.web.security;


import com.example.kafka.exception.AuthException;
import com.example.kafka.exception.BusinessException;
import com.example.kafka.model.constants.Constants;
import com.example.kafka.model.constants.ErrorCode;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TokenManager implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationInMs}")
    private Long expirationInMs;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) throws BusinessException {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.get("username").toString();
        } catch (Exception e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getErrorDesc());
        }
    }

    //retrieve username from jwt token
    public List<String> getRolesFromToken(String token) throws BusinessException {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return (List<String>) claims.get("roles");

        } catch (Exception e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getErrorDesc());
        }
    }



    public String getRoleFromSecurityContext(SecurityContext securityContext) throws BusinessException {
        Collection<GrantedAuthority> auths = (Collection<GrantedAuthority>) securityContext.getAuthentication().getAuthorities();
        List<String> userRoles = auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return userRoles.get(0);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getIssuerFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuer);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public List<String> generateToken(String username, List<String> roles) {
        List<String> result = new ArrayList<>();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", roles);
        String token = doGenerateToken(claims, Constants.SUBJECT, "WEB");
        result.add(token);
        return result;
    }



    private String doGenerateToken(Map<String, Object> claims, String subject, String issuer) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public boolean isValidFormat(String token) throws BusinessException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getErrorDesc());
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.EXPIRED_TOKEN.name(),
                    ErrorCode.EXPIRED_TOKEN.getErrorDesc());
        }
    }

    public void validateToken(String token) throws BusinessException {
        try {
            if (!isValidFormat(token)) {
                throw new AuthException(ErrorCode.INVALID_TOKEN.name(),
                        ErrorCode.INVALID_TOKEN.getErrorDesc());
            }
        } catch (Exception e) {
            throw new AuthException(ErrorCode.INVALID_TOKEN.name(),
                    ErrorCode.INVALID_TOKEN.getErrorDesc());
        }
    }


    public String getCurrentUser() {
        return getUsernameFromToken(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}


