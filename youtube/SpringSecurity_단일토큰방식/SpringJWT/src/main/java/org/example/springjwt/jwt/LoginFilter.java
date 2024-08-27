package org.example.springjwt.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.CustomUserDetails;
import org.example.springjwt.dto.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // 검증을 담당하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // JSON 요청인지 확인
        if ("application/json".equals(request.getContentType())) {
            try {
                // ObjectMapper를 사용해 JSON 데이터를 Java 객체로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                ServletInputStream inputStream = request.getInputStream();
                String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

                // LoginDTO는 요청을 담을 DTO 클래스
                LoginDTO loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

                // username과 password를 추출하여 UsernamePasswordAuthenticationToken 생성
                String username = loginDTO.getUsername();
                String password = loginDTO.getPassword();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

                // 인증 매니저에게 전달하여 인증 시도
                return authenticationManager.authenticate(authToken);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // JSON이 아닌 경우에도 로그인 처리 하도록 구현

            // username과 password를 추출하여 UsernamePasswordAuthenticationToken 생성
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // 정보를 담은 해당 Token 을 AuthenticationManager 로 전달
            return authenticationManager.authenticate(authToken);
        }

    }

    // 로그인 성공 시 자동 호출되어 실행되는 메서드
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){

        // Authentication 객체로부터 username 추출
        String username = authentication.getName();

        // CustomUserDetails 객체로부터 role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 추출한 username 과 role을 기반으로 JWT 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L); // 10분
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L); // 24시간

        //응답 설정
        response.setHeader("access", access); // access 토큰 헤더에 응답
        response.addCookie(createCookie("refresh", refresh)); // refresh 토큰 쿠키에 응답
        response.setStatus(HttpStatus.OK.value());

    }


    // 로그인 실패 시 자동 호출되어 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        // 401 상태코드 응답
        response.setStatus(401);
    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 생명 주기 설정
//        cookie.setSecure(true); // https 통신의 경우 설정
//        cookie.setPath("/"); // 쿠키 적용 범위 설정
        cookie.setHttpOnly(true); // 자바스크립트로 쿠키에 접근 못하도록 막기

        return cookie;
    }



}
