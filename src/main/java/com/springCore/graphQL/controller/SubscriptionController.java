package com.springCore.graphQL.controller;

import com.springCore.graphQL.entity.User;
import com.springCore.graphQL.publisher.SignupPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class SubscriptionController {
    @Autowired
    private SignupPublisher signupPublisher;

    @PreAuthorize("isAuthenticated()")
    @SubscriptionMapping
    public Flux<User> newUser() {
        return signupPublisher.getPublisher();
    }
}
