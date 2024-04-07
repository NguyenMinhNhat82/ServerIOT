package com.spring.iot.services;

import com.spring.iot.entities.User;
import com.spring.iot.repositories.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceCustom {
    @Autowired
    UserRepositoryCustom userRepositoryCustom;
    public List<User> getListUser(Map<String, String> params){
        return userRepositoryCustom.getListUser(params);
    }
}
