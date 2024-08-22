package org.example.springjwt.config;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // SecurityConfig에 AuthenticationConfiguration 주입. (생성자 주입)
    private final AuthenticationConfiguration authenticationConfiguration;

    // AuthenticationConfiguration를 통해 AuthenticationManager를 얻어오는 메서드 선언 + 그 AuthenticationManager 객체를 빈으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    } // 비밀번호 암호화 기능 사용

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll() // 모든 사용자 허용
                        .requestMatchers("/admin").hasRole("ADMIN") // admin 권한 사용자만 허용
                        .anyRequest().authenticated()); // 로그인한 사용자만 허용

        // 우리가 방금 만든 커스텀 필터 클래스인 LoginFilter 등록
        http
                .addFilterAt(new LoginFilter( authenticationManager(authenticationConfiguration) ), UsernamePasswordAuthenticationFilter.class);
                // 첫번째 인자: 우리가 등록하고자 하는 커스텀 필터 클래스,
                // 두번째 인자: 어느 위치에 등록할건지에 대한 필터 클래스.
                // 이 때, LoginFilter 생성 시에는 AuthenticatinoManager 가 필요하다.
                // authenticationManager 메서드를 호출함으로써 AuthenticationManager를 얻어 LoginFilter의 인자로 넣어준다.

        // 세션 설정(중요) : JWT를 통한 인증/인가를 위해 세션을 sateless 로 설정!
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


}
