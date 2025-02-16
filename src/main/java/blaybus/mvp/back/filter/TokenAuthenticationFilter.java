package blaybus.mvp.back.filter;

import blaybus.mvp.back.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(TOKEN_PREFIX)) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String accessToken = authorization.substring(TOKEN_PREFIX.length());

        // accessToken 검증
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            // 만료되었을 경우 로그아웃.

            // 로그아웃 정보 set ~~
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "토큰 만료됨.");

            //로그인 페이지로 redirect
            response.sendRedirect("http://localhost:8080/oauth2/authorization/google");
        }

        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return authorization.substring(TOKEN_PREFIX.length());
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
