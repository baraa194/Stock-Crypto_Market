package com.myProject.demo.Repositories;

import com.myProject.demo.Models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByUserUsername(String username);
}
