package com.myProject.demo.Services;

import com.myProject.demo.Exceptions.UserNotFoundException;
import com.myProject.demo.Models.User;
import com.myProject.demo.Models.UserPrinciple;
import com.myProject.demo.Repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user= userRepo.findByusername(username)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getRole().getName();

        return new UserPrinciple(user); // user principle is the user who try to log in
    }
}
