package com.spring.iot.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.iot.entities.User;
import com.spring.iot.repositories.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceCustom {
    @Autowired
    UserRepositoryCustom userRepositoryCustom;

    public List<User> getListUser(Map<String, String> params){
        return userRepositoryCustom.getListUser(params);
    }

}
