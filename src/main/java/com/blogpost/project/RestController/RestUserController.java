package com.blogpost.project.RestController;

import com.blogpost.project.model.Users;
import com.blogpost.project.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody  Users user){
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles("ROLE_AUTHOR");
        Boolean saveUserPresent = customUserDetailService.saveUserDetail(user);
        if(saveUserPresent){
            return "User Registered Success";
        }
        else {
            return "User Already Registered";
        }
    }
}
