package org.example.springjwt.service;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.JoinDTO;
import org.example.springjwt.entity.UserEntity;
import org.example.springjwt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) { // 보통 회원가입 여부(boolean)를 반환하지만, 본 강의에서는 간단하게 void를 반환한다.

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);
        if (isExist){
            return;
        }

        UserEntity data = new UserEntity();
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password)); // 암호화 진행
        data.setRole("ROLE_ADMIN");
        userRepository.save(data);
    }


}
