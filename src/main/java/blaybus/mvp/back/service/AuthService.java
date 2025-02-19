package blaybus.mvp.back.service;

import blaybus.mvp.back.jwt.GoogleTokenVerifier;
import blaybus.mvp.back.jwt.JwtProvider;
import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.repository.ClientRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final JwtProvider jwtProvider;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    // ✅ auth_code → id_token 변환
    public String exchangeAuthCodeForIdToken(String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("code", authCode);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", "POST_MESSAGE");
        params.put("grant_type", "authorization_code");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                params,
                Map.class
        );

        return response.getBody().get("id_token").toString();
    }

    // ✅ id_token 검증 후 JWT 발급
    public String authenticateWithGoogle(String authCode) {
        String idToken = exchangeAuthCodeForIdToken(authCode);
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(idToken);

        if (payload == null) {
            throw new RuntimeException("Invalid Google ID Token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // ✅ 유저 찾거나 생성 (Client 엔티티 구조에 맞게 수정)
        Client user = clientRepository.findByEmail(email)
                .orElseGet(() -> clientRepository.save(Client.builder()
                        .userId(email)  // ✅ userId 필드에 email을 저장
                        .name(name)
                        .email(email)
                        .role(Role.USER)  // ✅ 기본 ROLE 설정
                        .picture(picture)
                        .build()));

        // ✅ JWT 발급 후 반환
        return jwtProvider.createToken(user.getEmail(), user.getName());
    }
}
