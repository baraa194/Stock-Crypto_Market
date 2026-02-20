package com.myProject.demo.Services;

import com.myProject.demo.Models.User;
import com.myProject.demo.Models.UserPrinciple;
import com.myProject.demo.Repositories.UserRepo;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user= userRepo.findByusername(username)
               .orElseThrow(() -> new UsernameNotFoundException("Username not found"));


        return new UserPrinciple(user); // user principle is the user who try to log in
    }
}
