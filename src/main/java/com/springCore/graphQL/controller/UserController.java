package com.springCore.graphQL.controller;

import com.springCore.graphQL.entity.User;
import com.springCore.graphQL.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

    @QueryMapping
    public Optional<User> getUserById(@Argument int id){
        return userService.getUserById(id);
    }
    @QueryMapping
    public Optional<User> getUserByName(@Argument String username){
        return userService.getUserByName(username);
    }


}
