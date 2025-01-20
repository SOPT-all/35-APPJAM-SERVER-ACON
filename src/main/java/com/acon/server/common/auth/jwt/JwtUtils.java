package com.acon.server.common.auth.jwt;

import com.acon.server.common.auth.jwt.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(JwtConfig.class)
public class JwtUtils {

    private final SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 30L; // 30일
    private static final String JWT_KEY = "memberId";

    public JwtUtils(JwtConfig jwtConfig) {
        byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public List<String> createToken(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiryTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiryTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .claim(JWT_KEY, memberId)
                .setExpiration(accessTokenExpiryTime)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiryTime)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return List.of(accessToken, refreshToken);
    }
}
