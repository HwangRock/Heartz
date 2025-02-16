package blaybus.mvp.back.jwt;

import blaybus.mvp.back.domain.Client;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    //private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private static final String KEY_ROLE = "role";
    //private final TokenService tokenService;

    @PostConstruct
    private void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

//    // 1. refresh token 발급
//    public void generateRefreshToken(Authentication authentication, String accessToken) {
//        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
//        tokenService.saveOrUpdate(authentication.getName(), refreshToken, accessToken); // redis에 저장
//    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        log.info("authentication.getName(): {}", authentication.getName());
        log.info("authorities: {}", authorities);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    //해당 토큰의 authentication 정보 가져오기 위해 사용
    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

//    // 3. accessToken 재발급
//    public String reissueAccessToken(String accessToken) {
//        if (StringUtils.hasText(accessToken)) {
//            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
//            String refreshToken = token.getRefreshToken();
//
//            if (validateToken(refreshToken)) {
//                String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
//                tokenService.updateToken(reissueAccessToken, token);
//                return reissueAccessToken;
//            }
//        }
//        return null;
//    }


//    private Claims parseClaims(String token) {
//        try {
//            return Jwts.parser().verifyWith(secretKey).build()
//                    .parseSignedClaims(token).getPayload();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        } catch (MalformedJwtException e) {
//            throw new TokenException(INVALID_TOKEN);
//        } catch (SecurityException e) {
//            throw new TokenException(INVALID_JWT_SIGNATURE);
//        }
//    }

    public String getUserIdFromToken(final String token){
        String userId = getClaimFromToken(token, Claims::getSubject);
        return userId;
    }

    public String getRoleFromToken(final String token){
        String role =getClaimFromToken(token, claims -> claims.get("role", String.class));
        return role;
    }

    public Boolean isExpiredToken(final String token){
        Boolean isExpired = getClaimFromToken(token, Claims::getExpiration).before(new Date());
        return isExpired;
    }

//    public String getRole(final String token){
//        String role = String.valueOf(getAllClaimsFromToken(token).get("role"));
//        log.info("getUsernameFormToken subject = {}", role);
//        return role;
//    }

    /**
     * token 사용자 속성 정보 조회
     *
     * @param token JWT
     * @param claimsResolver Get Function With Target Claim
     * @param <T> Target Claim
     * @return 사용자 속성 정보
     */
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        // token 유효성 검증
        if(Boolean.FALSE.equals(validateToken(token)))
            return null;

        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    /**
     * token 사용자 모든 속성 정보 조회
     *
     * @param token JWT
     * @return All Claims
     */
    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token 검증
     *
     * @param token JWT
     * @return token 검증 결과
     */
    public Boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
