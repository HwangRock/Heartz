package blaybus.mvp.back.controller;

import blaybus.mvp.back.jwt.TokenProvider;
import blaybus.mvp.back.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ClientController {

    private final TokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("login-success")
    public ResponseEntity<?> handleGoogleOAuth(@RequestParam String code) {
        // ✅ 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // ✅ 구글 사용자 이메일 & 이름 가져오기
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // ✅ JWT 발급 (이메일 + 이름 포함)
        String jwtToken = jwtTokenProvider.generateToken(email, name);

        return ResponseEntity.ok(Collections.singletonMap("accessToken", jwtToken));
    }
}
