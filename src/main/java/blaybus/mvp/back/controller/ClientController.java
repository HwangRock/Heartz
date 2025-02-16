package blaybus.mvp.back.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value="")
public class ClientController {

    @Value("${GOOGLE_CLIENT}")
    private String GOOGLE_CLIENT;
    @Value("${GOOGLE_SECRET}")
    private String GOOGLE_SECRET;

//    @GetMapping("/api/vi/user/login-test")
//    public void loginTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String clientId = GOOGLE_CLIENT; // 구글 API Console에서 발급받은 클라이언트 ID
//        String redirectUri = "http://localhost:8080/login/oauth2/code/google"; // 승인된 리디렉션 URI
//        String scope = "https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile"; // 요청할 권한
//        String state = UUID.randomUUID().toString();;
//
//        //세션에 state 값 저장
//        HttpSession session = request.getSession();
//        session.setAttribute("oauth2_state", state);
//
//        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
//                "?client_id=" + clientId +
//                "&redirect_uri=" + redirectUri +
//                "&response_type=code" +
//                "&scope=" + scope +
//                "&state=" + state;
//
//
//        //String url = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope + "&state=" +state;
//        response.sendRedirect(authUrl);
//    }


    @GetMapping("/test")
    public void test01(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
    }

}
