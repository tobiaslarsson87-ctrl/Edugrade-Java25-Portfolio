package se.edugrade.java25.enterprise.gym.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private static final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000;
    private final SecretKey secretKey;

    public JwtUtil(@Value("Qm9uZVNlY3VyZUtleUdlbmVyYXRlZEF0UmFuZG9tQnl0ZXMyNTZCaXRzMTIzNDU2Nzg5") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, List<String>roles) {
        Date now = new Date();
        Date expires = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expires)
                .signWith(secretKey)
                .compact();
    }

    public Claims validateClaim(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return validateClaim(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claim = validateClaim(token);
        Object roles = claim.get("roles");
        if (roles instanceof List<?>) return (List<String>) roles;
        return List.of();
    }

    public boolean isExpired(String token) {
        try {
            Date expiration = validateClaim(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public long validityInSeconds() {
        return ACCESS_TOKEN_VALIDITY / 1000;
    }
}
