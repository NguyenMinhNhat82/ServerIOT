package com.spring.iot.controllers;

import com.spring.iot.dto.HelloResponse;
import com.spring.iot.entities.User;
import com.spring.iot.services.UserService;
import com.spring.iot.services.jwt.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public HelloResponse hello() {
        return new HelloResponse("Hello from JWT Authorization");
    }

    @GetMapping(value = "/current-user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> currentUser(Principal user){
        User u = userService.findUserByEmail(user.getName());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }



}
