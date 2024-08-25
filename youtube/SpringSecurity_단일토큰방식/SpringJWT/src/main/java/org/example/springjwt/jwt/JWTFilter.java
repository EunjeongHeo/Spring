package org.example.springjwt.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.springjwt.dto.CustomUserDetails;
import org.example.springjwt.entity.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil; // JWTUtil 사용하기 위해 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request 헤더에서 jwt 토큰 추출
        String authorization = request.getHeader("Authorization");
        System.out.println("1. " + authorization);

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response); // 다음 필터로 요청, 응답 전달
            return; // 헤더 검증 실패 시 메서드 종료 (필수)
        }

        // Bearer 제거 후 순수 토큰 추출
        String token = authorization.split(" ")[1]; // 요청 헤더에서 추출한 jwt 토큰을 공백을 기준으로 분리하여 생성된 배열에서 두 번째 인자 추출 <= ["Bearer", "토큰값"]

        // 토큰 소멸 여부 검증
        if (jwtUtil.isExpired(token)) {
            System.out.printf("token expired");
            filterChain.doFilter(request, response); // 다음 필터로 요청, 응답 전달
            return; // 토큰 만료 시 메서드 종료 (필수)
        }

        // 토큰에서 username 과 role 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // 추출한 정보로 userEntity 생성
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("temppassword") // 토큰에 비밀번호는 포함되지 않는다. 비밀번호 검증이 필요없다. 우리가 임의로 설정해주면 된다.
                .role(role)
                .build();

        // UserDetails 에 userEntity 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // 시큐리티 인증 토큰 객체 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록 (임시 저장)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청, 응답 전달
        filterChain.doFilter(request, response);

        return;

    }


}
