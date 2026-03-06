package com.myProject.demo.Services;

import com.myProject.demo.DTO.LoginDTO;
import com.myProject.demo.DTO.RegisterRequest;
import com.myProject.demo.Models.Role;
import com.myProject.demo.Models.User;
import com.myProject.demo.Repositories.RoleRepo;
import com.myProject.demo.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private AuthenticationManager authmanager;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private RoleRepo roleRepo;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    public void register(RegisterRequest request){
        User user = mapToUser(request);
        repo.save(user);
    }

    private User mapToUser(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());


        if(request.getRole() != null){
            Role role = roleRepo.findByname(request.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        return user;
    }
    public String verify(LoginDTO loginDTO){

        Authentication authentication = authmanager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(loginDTO.getUsername(),role);
        }

        return "failed";
    }



}
