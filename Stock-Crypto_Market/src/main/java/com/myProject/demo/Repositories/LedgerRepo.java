package com.myProject.demo.Repositories;

import com.myProject.demo.Models.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepo extends JpaRepository<LedgerEntry,Long> {
List<LedgerEntry> findByWalletIdOrderByCreatedAtDesc(Long walletId);





}
