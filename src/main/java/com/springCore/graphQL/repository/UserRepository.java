package com.springCore.graphQL.repository;

import com.springCore.graphQL.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {




    Optional<User> findByUserId(int pId);
    Optional<User> findByUserName(String name);


}
