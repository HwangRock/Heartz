package blaybus.mvp.back.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuthRequest {
    private String email;    // ✅ 사용자 이메일
    private String name;     // ✅ 사용자 이름
}
