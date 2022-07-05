package com.blogpost.project.service;

import com.blogpost.project.model.Users;
import com.blogpost.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new MyUserPrincipal(user);
    }

    public void saveUserDetail(Users users){
        userRepository.save(users);
    }
}
