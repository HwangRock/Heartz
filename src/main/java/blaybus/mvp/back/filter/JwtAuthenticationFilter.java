package blaybus.mvp.back.filter;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.dto.response.CustomUserDetails;
import blaybus.mvp.back.jwt.TokenProvider;
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
                String email = jwtTokenProvider.getEmail(token);

                /**********    추가     **********/
                //Client객체 생성.
                Client client = Client.builder()
                        .name(username)
                        .email(email)
                        .role(Role.USER)
                        .build();

                CustomUserDetails customUserDetails = new CustomUserDetails(client);

                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
                /**********    추가     **********/

                // SecurityContext 설정 가능 (필요 시 구현)
                //SecurityContextHolder.getContext().setAuthentication(null); // 필요에 따라 인증 설정
            }
        }

        filterChain.doFilter(request, response);
    }
}

