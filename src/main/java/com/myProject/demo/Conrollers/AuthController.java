package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.LoginDTO;
import com.myProject.demo.DTO.RegisterRequest;
import com.myProject.demo.Models.User;
import com.myProject.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void Register(@RequestBody RegisterRequest regreq){
        userService.register(regreq);
        System.out.println("Register Success");
    }

    @PostMapping("/login")
    public String Login(@RequestBody LoginDTO loginDTO){
        return userService.verify(loginDTO);

    }
}
