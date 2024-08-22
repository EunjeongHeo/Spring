package org.example.springjwt.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor // AuthenticationManager 에 대한 생성자 주입
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    // 검증을 담당하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 추출한 username 과 password 를 UsernamePasswordAuthenticationToken 타입에 담는다. (스프링 시큐리티에서 username 과 password 를 검증하기 위해서 해당 타입으로의 구현이 필요함)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null); // 원래는 마지막 인자에 role 값 같은 게 들어가야 한다.

        // 정보를 담은 해당 Token 을 AuthenticationManager 로 전달 (우리는 이 Token 을 넘겨주기만 하면 AuthenticationManager 가 자동으로 검증을 수행해준다.)
        return authenticationManager.authenticate(authToken);

    }

    // 로그인 성공 시 자동 호출되어 실행되는 메서드
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        // JWT 발급 로직 구현
    }


    // 로그인 실패 시 자동 호출되어 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        // 실패 처리 로직 구현
    }



}
