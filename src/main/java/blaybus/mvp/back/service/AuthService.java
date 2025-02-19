package blaybus.mvp.back.service;

import blaybus.mvp.back.jwt.JwtProvider;
import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final JwtProvider jwtProvider;

    // ✅ 프론트에서 access_token으로 가져온 사용자 정보를 백엔드에서 바로 저장 & JWT 발급
    public String authenticateWithGoogle(String email, String name) {
        // ✅ 유저 찾거나 생성
        Client user = clientRepository.findByEmail(email)
                .orElseGet(() -> clientRepository.save(Client.builder()
                        .userId(email)  // ✅ userId에 email 저장
                        .name(name)
                        .email(email)
                        .role(Role.USER)  // ✅ 기본 ROLE 설정
                        .picture("pic")
                        .build()));

        // ✅ JWT 발급 후 반환
        return jwtProvider.createToken(user.getEmail(), user.getName());
    }
}
