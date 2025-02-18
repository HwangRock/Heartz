package blaybus.mvp.back.oauth;

import blaybus.mvp.back.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider jwtTokenProvider;

    public CustomOAuth2SuccessHandler(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // ✅ 구글 사용자 이메일 & 이름 가져오기
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name"); // ✅ name 추가

        // ✅ JWT 토큰 생성 (이름도 함께 포함)
        String jwtToken = jwtTokenProvider.generateToken(email, name);

        // ✅ 이전 페이지(state) 가져오기
        String state = request.getParameter("state");
        if (state != null && !state.isEmpty()) {
            // ✅ JWT 토큰을 URL에 포함해서 이전 페이지로 리디렉트
            String redirectUrl = state + "?token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            // ✅ 기본 리디렉트 (메인 페이지)
            String redirectUrl = "/?token=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}
