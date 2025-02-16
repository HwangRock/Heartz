package blaybus.mvp.back.config;

import blaybus.mvp.back.filter.TokenAuthenticationFilter;
import blaybus.mvp.back.filter.TokenExceptionFilter;
import blaybus.mvp.back.handler.CustomAccessDeniedHandler;
import blaybus.mvp.back.handler.CustomAuthenticationEntryPoint;
import blaybus.mvp.back.handler.OAuth2FailureHandler;
import blaybus.mvp.back.handler.OAuth2SuccessHandler;
import blaybus.mvp.back.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

        // white list (Spring Security 체크 제외 목록)
        MvcRequestMatcher[] permitAllWhiteList = {
                mvc.pattern("/api/vi/user/login-test"),
                mvc.pattern("/login/oauth2/code/google"),
                //mvc.pattern("/token-refresh"),
                mvc.pattern("/favicon.ico"),
                mvc.pattern("/error")
        };


        http
                .csrf(
                        (csrfConfig) -> csrfConfig.disable()
                )
        //Form 로그인 방식 disable
                .formLogin((auth) -> auth.disable())
        //http basic 인증 방식 disable
                .httpBasic((auth) -> auth.disable())
                .headers(
                        (headerConfig) -> headerConfig.frameOptions(
                                frameOptionsConfig -> frameOptionsConfig.disable()
                        )
                )
                .authorizeHttpRequests((authorizeRequest) -> authorizeRequest
                        .requestMatchers(permitAllWhiteList).permitAll()
                        .requestMatchers("/test", "/oauth2/authorization/google", "/login/oauth2/code/google", "/error", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //이 부분 수정ㅈ~~~~~~~~~~~~~~~~~~~~~
                .logout( // 로그아웃 성공 시 / 주소로 이동
                        (logoutConfig) -> logoutConfig.logoutSuccessUrl("/")
                )
                // OAuth2 로그인 기능에 대한 여러 설정
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                        oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                                // 로그인 성공 시 핸들러
                                .successHandler(oAuth2SuccessHandler)
                                // 로그인 실패 시 핸들러
                                .failureHandler(new OAuth2FailureHandler())
                                //로그인 실패 시 리다이렉트할 로그인 페이지
                                //.loginPage("/api/vi/user/login-test")
                )
                // jwt 관련 설정
                .addFilterBefore(tokenAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        /*
                .oauth2Login(
                        (oauth) ->
                            oauth.userInfoEndpoint(
                                    (endpoint) -> endpoint.userService(customOAuth2UserService)
                            )
                );
        */

        return http.build();
    }

}
