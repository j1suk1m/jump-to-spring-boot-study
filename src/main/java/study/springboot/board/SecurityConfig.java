package study.springboot.board;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화 -> 모든 요청 URL 제어
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용 활성화
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 인증되지 않은 모든 페이지의 요청 허용
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))) // /h2-console/로 시작하는 모든 URL의 CSRF 검증 제외
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) // X-Frame-Options 헤더를 SAMEORIGIN으로 설정
                .formLogin(formLogin -> formLogin
                        .usernameParameter("name")
                        .passwordParameter("password")
                        .loginPage("/member/login") // 로그인 페이지 URL
                        .defaultSuccessUrl("/")) // 로그인 성공 시 이동할 URL
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 페이지 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL
                        .invalidateHttpSession(true)); // 세션 삭제

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 스프링 시큐리티 인증 및 권한 부여 프로세스 처리
     * @param authenticationConfiguration
     * @return AuthenticationManager 객체
     * @throws Exception
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}