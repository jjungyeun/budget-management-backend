package com.wonjung.budget.security;

import com.wonjung.budget.exception.CustomException;
import com.wonjung.budget.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final long accessTokenValidityInMilliseconds;

    public JwtUtils(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.expiration-in-seconds}") final long accessTokenValidityInMilliseconds
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
    }

    public String createToken(Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());

        } catch (ExpiredJwtException ex) {
            throw new CustomException(ErrorCode.EXPIRE_TOKEN);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Long getMemberId(String token) {
        if (!validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String payload = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(payload);
    }
}
