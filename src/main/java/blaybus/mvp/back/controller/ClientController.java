package blaybus.mvp.back.controller;

import blaybus.mvp.back.jwt.TokenProvider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ClientController {

    private final TokenProvider tokenProvider;

    public ClientController(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // OAuth2 인증 성공 후 호출되는 엔드포인트
    @GetMapping("/login-success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");

        // JWT 생성
        String token = tokenProvider.createToken(username, email);

        // Bearer 토큰 형태로 반환
        return "Bearer " + token;
    }

    // 토큰 생성 테스트용 API
    @GetMapping("/test-token")
    public String testToken() {
        // Mock 데이터
        String username = "test_user";
        String email = "test_user@example.com";

        // JWT 생성
        String token = tokenProvider.createToken(username, email);

        // Bearer 토큰 형태로 반환
        return "Bearer " + token;
    }

}
