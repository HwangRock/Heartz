package blaybus.mvp.back.handler;

import blaybus.mvp.back.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TokenProvider tokenProvider;

    private final String REDIRECT_URI = "/login/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        HttpSession session = request.getSession();

        // 세션에서 저장된 state 값과 요청에서 받은 state 값 비교
//        String receivedState = request.getParameter("state");
//        String expectedState = (String) session.getAttribute("oauth2_state");
//
//        if (expectedState == null || !expectedState.equals(receivedState)) {
//            // CSRF 공격 가능성, 요청 거부
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid state parameter.");
//            return;
//        }

        // accessToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        //tokenProvider.generateRefreshToken(authentication, accessToken);

        //response set
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + accessToken);

        log.info("accessToken: {}", accessToken);

        String jsonResponse = "{\"message\": \"Success\"}"; // 예시 JSON 응답
        response.getWriter().write(jsonResponse);
        //response.flushBuffer(); // 응답 전송

        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);


        //response.sendRedirect(REDIRECT_URL);

//        String redirectUrlWithToken = REDIRECT_URL + "?accessToken=" + accessToken;
//        redirectStrategy.sendRedirect(request, response, redirectUrlWithToken);

        //redirectStrategy.sendRedirect(request, response, getRedirectUrl(accessToken));

        //redirect url 설정
        // 접근 권한 없는 경로 접근해서 스프링 시큐리티가 인터셉트해서 로그인폼으로 이동 후 로그인 성공한 경우
//        if (savedRequest != null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            log.info("targetUrl = {}", targetUrl);
//            redirectStrategy.sendRedirect(request, response, targetUrl);
//        }
//        // 로그인 버튼 눌러서 로그인한 경우 기존에 있던 페이지로 리다이렉트
//        else {
//            String prevPage = (String) request.getSession().getAttribute("prevPage");
//            log.info("prevPage = {}", prevPage);
//            redirectStrategy.sendRedirect(request, response, prevPage);
//        }
    }

//    private String getRedirectUrl(String accessToken){
//        return UriComponentsBuilder.fromUriString(REDIRECT_URL)
//                .queryParam("access_token", accessToken)
//                .build().toUriString();
//    }

}
