package com.blogpost.project.controller;

import com.blogpost.project.model.Users;
import com.blogpost.project.security.ApplicationSecurityConfig;
import com.blogpost.project.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/registerUser")
    public String registerUser(Users user){
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles("ROLE_AUTHOR");
        customUserDetailService.saveUserDetail(user);
        return "redirect:/loginPage";
    }

    @GetMapping("/registerPage")
    public String showRegisterPage(@ModelAttribute("user") Users user){
        return "signup";
    }

    @GetMapping("/loginPage")
    public String login(){
        return "loginPage";
    }

}
