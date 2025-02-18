package blaybus.mvp.back.filter;

import blaybus.mvp.back.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                // 토큰에서 사용자 정보 추출 가능
                String username = jwtTokenProvider.getUsername(token);

                // SecurityContext 설정 가능 (필요 시 구현)
                SecurityContextHolder.getContext().setAuthentication(null); // 필요에 따라 인증 설정
            }
        }

        filterChain.doFilter(request, response);
    }
}

