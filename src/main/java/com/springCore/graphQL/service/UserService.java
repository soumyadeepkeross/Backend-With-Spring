package com.springCore.graphQL.service;


import com.springCore.graphQL.entity.User;
import com.springCore.graphQL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;


    public List<User> getUsers(){
        return userRepo.findAll();
    }


    public Optional<User> getUserById(int uid){
        return userRepo.findByUserId(uid);
    }

    public Optional<User> getUserByName(String username){
        return userRepo.findByUserName(username);
    }
}
