package com.springCore.graphQL.controller;


import com.springCore.graphQL.entity.User;
import com.springCore.graphQL.model.AuthPayload;
import com.springCore.graphQL.model.LoginInput;
import com.springCore.graphQL.model.SignupInput;
import com.springCore.graphQL.model.UserPayload;
import com.springCore.graphQL.publisher.SignupPublisher;
import com.springCore.graphQL.repository.UserRepository;
import com.springCore.graphQL.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.Optional;

@Controller
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SignupPublisher signupPublisher;

    @MutationMapping
    public AuthPayload login(@Argument LoginInput loginInput) {
        // Authenticate the user
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginInput.username(), loginInput.password())
            );
            if (authentication.isAuthenticated()) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(loginInput.username());
                final String token = jwtUtil.generateToken(userDetails);
                return new AuthPayload(token, "N/A");
            }
        } catch (Exception e) {
            System.out.println("Error occured : " + e.getMessage());
        }
        return new AuthPayload("N/A","Invalid username / password");
    }



    @MutationMapping
    public UserPayload signup(@Argument SignupInput signupInput){
        Optional<User> existingUser = repo.findByUserName(signupInput.username());
        if(existingUser.isPresent()){
            System.out.println("user present");
            return new UserPayload(signupInput.username(),signupInput.password(),signupInput.role(),"Failed");
        }
        User newUser = repo.save(new User(signupInput.username(), passwordEncoder.encode(signupInput.password()),signupInput.role()));
        System.out.println(newUser.getUserName() + newUser.getPassword()  + newUser.getRole());
        signupPublisher.publish(newUser);
        return new UserPayload(newUser.getUserName(),newUser.getPassword(),newUser.getRole(),"OK");
    }


}
