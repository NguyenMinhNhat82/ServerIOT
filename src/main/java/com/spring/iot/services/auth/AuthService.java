package com.spring.iot.services.auth;


import com.spring.iot.dto.SignupDTO;
import com.spring.iot.dto.UserDTO;

public interface AuthService {
    UserDTO createUser(SignupDTO signupDTO);
}
