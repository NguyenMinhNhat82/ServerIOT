package com.spring.iot.controllers;

import com.spring.iot.entities.User;
import com.spring.iot.services.UserService;
import com.spring.iot.services.UserServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceCustom userServiceCustom;


    @GetMapping("/api/user/get0-all")
    public ResponseEntity<List<User>> getAllUser(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(userServiceCustom.getListUser(params), HttpStatus.OK);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("idUSer") Long idUser, Principal user) {
        try {
            User u = userService.findUserById(idUser);
            User checkUser = userService.findUserByEmail(user.getName());
            if (checkUser.getId().equals(u.getId()))
                return new ResponseEntity<>("Bạn không thể xóa tài khoản của chính bạn", HttpStatus.OK);
            userService.delete(u);

            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/user/edit-user")
    public ResponseEntity<?> editUser(@RequestBody Map<String, String> req) {
        User user = new User(Long.parseLong(req.get("id")), req.get("name"), req.get("email"), "", req.get("role"));
        User update = userService.findUserById(user.getId());
        if (update != null) {
            if (!update.getRole().equals(user.getRole()))
                return new ResponseEntity<>("Không được thay đổi quyền của chình mình", HttpStatus.OK);
            user.setPassword(update.getPassword());
            userService.save(user);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/user/create-user")
    public ResponseEntity<?> addUser(@RequestBody Map<String, String> req) {
        try {
            User user = new User(0L, req.get("name"), req.get("email"), req.get("password"), req.get("role"));

            User checkUser = userService.findUserByEmail(user.getEmail());
            if (checkUser != null) {
                return new ResponseEntity<>("Email đã được đăng kí từ trước", HttpStatus.OK);
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userService.save(user);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }

    }

    @GetMapping("/api/user/test")
    public ResponseEntity<List<User>> getAllUserTest(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(userServiceCustom.getListUser(params), HttpStatus.OK);
    }


}
