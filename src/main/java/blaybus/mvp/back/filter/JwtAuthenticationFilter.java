package blaybus.mvp.back.filter;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtProvider.validateToken(token)) {
                // ✅ 토큰에서 사용자 정보 추출 (userId, role 제거)
                String email = jwtProvider.getEmailFromToken(token);
                String name = jwtProvider.getNameFromToken(token);

                //Client객체 생성.
                Client client = Client.builder()
                        .name(name)
                        .email(email)
                        .role(Role.USER)
                        .build();

                CustomUserDetails customUserDetails = new CustomUserDetails(client);

                // ✅ Spring Security 인증 객체 생성
                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}

