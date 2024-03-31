package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}

