package blaybus.mvp.back.dto;

import blaybus.mvp.back.domain.Client;

import java.io.Serializable;

public class SessionUser implements Serializable { // 직렬화 기능을 가진 세션 DTO

    // 인증된 사용자 정보만 필요 => name, email, picture 필드만 선언
    private String name;
    private String email;
    private String picture;

    public SessionUser(Client client) {
        this.name = client.getName();
        this.email = client.getEmail();
        this.picture = client.getPicture();
    }
}
