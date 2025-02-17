package blaybus.mvp.back.service;

import blaybus.mvp.back.domain.Client;
import blaybus.mvp.back.dto.OAuthAttributes;
import blaybus.mvp.back.dto.PrincipalDetails;
import blaybus.mvp.back.repository.ClientRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final ClientRepository clientRepository;
    private final HttpSession httpSession;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        Map<String, Object> oAuth2User = delegate.loadUser(userRequest).getAttributes();

        //1. registrationId 가져오기(third-party id)
        // 로그인 진행 중인 서비스를 구분
        // 네이버로 로그인 진행 중인지, 구글로 로그인 진행 중인지, ... 등을 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //2. userNameAttributeName 가져오기
        // OAuth2 로그인 진행 시 키가 되는 필드 값(Primary Key와 같은 의미)
        // 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id".
        // 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        //3. 유저 정보 dto 생성
        //  OAuth2 로그인을 통해 가져온 OAuth2User의 attribute 등을 담을 클래스
        // oAuth2User.getAttributes() 에는 다음 값이 담긴다.
        // 구글 예시) { sub=1231344534523565757, name=홍길동, given_name=길동, family_name=홍, picture=https://xxx, email=xxx@gmail.com, email_verified=true, locale=ko}
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User);

        //4. 회원가입 및 로그인
        // 사용자 저장 또는 업데이트
        Client client = saveOrUpdate(attributes);

        //httpSession.setAttribute("client", new SessionUser(client));

        //5. OAuth2User로 반환
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(client.getRoleKey())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
        return new PrincipalDetails(client, oAuth2User, userNameAttributeName);
    }

    private Client saveOrUpdate(OAuthAttributes attributes) {
        Client client = clientRepository.findByEmail(attributes.getEmail())
                // 구글 사용자 정보 업데이트(이미 가입된 사용자) => 업데이트
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                // 가입되지 않은 사용자 => User 엔티티 생성
                .orElse(attributes.toEntity());

        return clientRepository.save(client);
    }
}
