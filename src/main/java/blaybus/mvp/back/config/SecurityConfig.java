package blaybus.mvp.back.config;

import blaybus.mvp.back.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final TokenProvider jwtTokenProvider;

    public SecurityConfig(TokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 새롭게 변경된 CSRF 비활성화 방식
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/oauth2/authorization/google", "/login/oauth2/code/google", "/error", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/login-success", true)
                );

        return http.build();
    }
}
