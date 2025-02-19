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
        String jwt = authService.authenticateWithGoogle(request.getAuthCode());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
