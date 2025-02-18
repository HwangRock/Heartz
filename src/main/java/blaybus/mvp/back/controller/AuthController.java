package blaybus.mvp.back.controller;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.dto.request.GoogleOAuthRequest;
import blaybus.mvp.back.dto.response.JwtResponse;
import blaybus.mvp.back.jwt.JwtProvider;
import blaybus.mvp.back.service.ClientService;
import blaybus.mvp.back.jwt.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final ClientService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleOAuthRequest request) {
        // 1. Google ID 토큰 검증
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getIdToken());
        if (payload == null) {
            return ResponseEntity.status(401).body("Invalid Google ID Token");
        }

        // 2. 유저 정보 추출
        String userId = payload.getSubject(); // Google OAuth의 sub 값 (유저 고유 ID)
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // 3. 유저 찾거나 생성
        Client user = userService.findOrCreateUser(userId, name, email, picture);

        // 4. JWT 생성 및 반환
        String jwt = jwtProvider.createToken(user.getUserId(), user.getEmail());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
