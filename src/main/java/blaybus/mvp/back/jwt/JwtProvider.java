package blaybus.mvp.back.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")  // ✅ 환경변수에서 JWT 시크릿 키 가져오기
    private String secretKey;

    private final long expirationMs = 86400000; // 24시간 (1일)

    // ✅ JWT 생성 (email, name만 포함)
    public String createToken(String email, String name) {
        return Jwts.builder()
                .setSubject(email)  // email을 subject로 설정
                .claim("name", name) // 이름 저장
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ✅ 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ 토큰에서 email 추출
    public String getEmailFromToken(String token) {
        return parseToken(token).getSubject();
    }

    // ✅ 토큰에서 name 추출
    public String getNameFromToken(String token) {
        return parseToken(token).get("name", String.class);
    }

    // ✅ 토큰 파싱
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
