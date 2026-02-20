package com.myProject.demo.Repositories;

import com.myProject.demo.DTO.WalletRequest;
import com.myProject.demo.DTO.WalletResponse;
import com.myProject.demo.Models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepo extends JpaRepository<Wallet,Long> {

    @Query(" select new com.myProject.demo.DTO.WalletResponse (w.user.id,w.balance,w.currency,w.updatedAt) from Wallet w ")
    List<WalletResponse> findAllWallets();



    Wallet findByUserUsername(@Param("username") String username);


}
