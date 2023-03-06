package com.project.secretdiary.jwt;

import com.project.secretdiary.dto.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long acExpireTime;

    @Value("${security.jwt.token.refresh-toekn-expire-length}")
    private long reExpireTime;
    private final CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public TokenResponse createToken(String id) {
        Claims claims = Jwts.claims().setSubject(id);

        Date now = new Date();
        Date expiresIn = new Date(now.getTime()+acExpireTime);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+reExpireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        TokenResponse tokenResponse = new TokenResponse(accessToken,refreshToken,reExpireTime);
        return tokenResponse;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(getId(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getId(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return expiration.getTime() - now;
    }

}