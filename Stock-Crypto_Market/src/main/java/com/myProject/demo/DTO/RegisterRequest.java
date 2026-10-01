package com.myProject.demo.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters and contain one uppercase letter and one number"
    )
    private String password;
    private String phoneNumber;
    private String address;
    private String role;
}
