package io.mkolodziejczyk92.anonymoussanta.data.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAll(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAll(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isValidForUser(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUserName(token).equals(userDetails.getUsername());
    }

    private String generateToken(Map<String, Objects> claimsMap, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claimsMap)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(new Date().getTime() + 60 * 60 * 24 * 10000))
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


}