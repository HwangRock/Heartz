package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.domain.Role;
import blaybus.mvp.back.jwt.TokenProvider;
import blaybus.mvp.back.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ClientRepository clientRepository;
    private final TokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // ✅ 구글 사용자 정보 가져오기
        String userId = oAuth2User.getAttribute("sub"); // ✅ 구글 ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // ✅ 기존 유저 조회 or 신규 회원가입
        Client user = clientRepository.findByEmail(email)
                .map(existingUser -> existingUser.update(name, picture)) // ✅ 기존 회원이면 정보 업데이트
                .orElseGet(() -> clientRepository.save(Client.builder()
                        .userId(userId) // ✅ 구글 ID 저장
                        .name(name)
                        .email(email)
                        .picture(picture)
                        .role(Role.USER) // ✅ 기본 권한 ROLE_USER
                        .build()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
