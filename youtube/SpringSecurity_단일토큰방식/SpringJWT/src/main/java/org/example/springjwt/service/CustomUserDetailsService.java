package org.example.springjwt.service;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.CustomUserDetails;
import org.example.springjwt.entity.UserEntity;
import org.example.springjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        if (userData == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // UserDetails 에 담아서 return 하면 AuthenticationManager 가 검증함
        return new CustomUserDetails(userData);
    }
}

