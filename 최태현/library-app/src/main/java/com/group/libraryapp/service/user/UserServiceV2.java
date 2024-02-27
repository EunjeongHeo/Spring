package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}


