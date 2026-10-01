package com.myProject.demo.Repositories;

import com.myProject.demo.Models.PortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepo  extends JpaRepository<PortfolioItem, Long> {
    List<PortfolioItem> findByPortfolioId(Long portfolioId);
    Optional<PortfolioItem> findByPortfolioIdAndAssetName(long portfolioId, String assetName);
}
