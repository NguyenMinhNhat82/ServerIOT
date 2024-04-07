package com.spring.iot.controllers;

import com.spring.iot.dto.AuthenticationResponse;
import com.spring.iot.dto.SignupDTO;
import com.spring.iot.dto.UserDTO;
import com.spring.iot.entities.User;
import com.spring.iot.services.UserService;
import com.spring.iot.services.auth.AuthService;
import com.spring.iot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class SignupController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

        @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
       UserDTO createdUser = authService.createUser(signupDTO);
       if (createdUser == null){
           return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/checkusername/")
    ResponseEntity<Boolean> checkEmail(@RequestBody Map<String,String> request){
        User user = userService.findUserByEmail(request.get("email"));
        if(user == null)
            return new ResponseEntity<>(false,HttpStatus.OK);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @PostMapping("/token-sign-up")
    ResponseEntity<AuthenticationResponse> generateTokenForSignUp(@RequestBody User authenticationDTO){
        UserDetails user  = new org.springframework.security.core.userdetails.User(authenticationDTO.getEmail(),authenticationDTO.getPassword(), new ArrayList<>());
        final String jwt = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(new AuthenticationResponse(jwt),HttpStatus.OK);
    }

}
