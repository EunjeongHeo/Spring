package org.example.springjwt.repository;

import org.example.springjwt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByUsername(String username); // username 기반으로 회원 존재 여부 반환
}