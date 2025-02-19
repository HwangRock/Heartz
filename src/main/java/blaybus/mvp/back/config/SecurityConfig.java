package blaybus.mvp.back.config;

import blaybus.mvp.back.jwt.JwtProvider;
import blaybus.mvp.back.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정을 가장 먼저 적용
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (JWT 사용하므로 불필요)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 세션 비활성화 (JWT 사용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // ✅ Preflight 요청 허용
                        .requestMatchers(
                                "/",
                                "/login/oauth2/code/google",
                                "/error",
                                "/favicon.ico",
                                "/api/v1/auth/test-token",
                                "/api/v1/designer/**",
                                "/api/v1/schedule/**",
                                "/api/v1/auth/google", // ✅ Google OAuth 로그인 API 허용
                                "/api/v1/reservation/createReservation",
                                "/designer-list"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class); // ✅ JWT 인증 필터 적용

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ 로컬 개발 & 배포 환경 모두 허용
        configuration.setAllowedOrigins(List.of(
                "https://heartz4.vercel.app", // 배포 환경
                "http://localhost:5173"
        ));

        // ✅ 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));

        // ✅ 인증 포함 요청 허용 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
