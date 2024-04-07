package com.spring.iot.controllers;


import com.spring.iot.services.MailService;
import com.spring.iot.services.OTPService;
import com.spring.iot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class OTPController {
    @Autowired
    public OTPService otpService;

    @Autowired
    public MailService emailService;

    @Autowired
    public UserService userService;

    @PostMapping("/generateOtp")
    public ResponseEntity<Integer> generateOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        int otp = otpService.generateOTP(email);
        //Email
        emailService.sendMail(request.get("name"), email, otp);
        if (otp < 0)
            return new ResponseEntity<>(0, HttpStatus.OK);
        return new ResponseEntity<>(otp, HttpStatus.OK);
    }

    @RequestMapping(value = "/validateOtp", method = RequestMethod.POST)
    public ResponseEntity<String> validateOtp(@RequestParam("otpnum") int otpnum, @RequestBody Map<String, String> request) {
        String username = request.get("email");
        if (otpnum >= 0) {

            int serverOtp = otpService.getOtp(username);
            if (serverOtp > 0) {
                if (otpnum == serverOtp) {
                    otpService.clearOTP(username);
                    return new ResponseEntity<>("VALID", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("NOT MATCH", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("INVALID", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("WRONG", HttpStatus.OK);
        }
    }
}
