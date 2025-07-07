package com.springCore.graphQL.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DemoController {

    @QueryMapping
    public String getResult(){
        return "This is GET query of GraphQL";
    }
}
