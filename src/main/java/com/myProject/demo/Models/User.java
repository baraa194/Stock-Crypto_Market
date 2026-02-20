package com.myProject.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_name", nullable = false)
    private String username;
    @Column( nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(nullable = false)
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;



    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Trade> trades;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "user")
    @JsonIgnore
    private Wallet wallet;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
     private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Portfolio>  portfolios;




}
