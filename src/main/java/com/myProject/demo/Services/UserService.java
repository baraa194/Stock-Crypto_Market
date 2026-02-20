package com.myProject.demo.Services;

import com.myProject.demo.DTO.LoginDTO;
import com.myProject.demo.Models.User;
import com.myProject.demo.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private AuthenticationManager authmanager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    public void register(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public String verify(LoginDTO loginDTO){

        Authentication authentication = authmanager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(loginDTO.getUsername());
        }

        return "failed";
    }



}
