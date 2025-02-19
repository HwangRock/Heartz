package blaybus.mvp.back.controller;

import blaybus.mvp.back.service.AuthService;
import blaybus.mvp.back.dto.request.GoogleOAuthRequest;
import blaybus.mvp.back.dto.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> googleLogin(@RequestBody GoogleOAuthRequest request) {
        // ✅ 프론트에서 access_token으로 가져온 사용자 정보를 받아서 처리
        String jwt = authService.authenticateWithGoogle(
                request.getEmail(),
                request.getName()
        );
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
