package com.blogpost.project.service;

import com.blogpost.project.Serviceimplementation.MyUserPrincipal;
import com.blogpost.project.model.Users;
import com.blogpost.project.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findUserByName(username);
        return new MyUserPrincipal(user.get());
    }

    public Boolean saveUserDetail(Users users){
        try{
            userRepository.save(users);
            return true;
        }
        catch (ConstraintViolationException | DataIntegrityViolationException exception){
            return false;
        }
    }

}
