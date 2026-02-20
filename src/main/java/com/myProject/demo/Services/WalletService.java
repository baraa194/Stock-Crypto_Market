package com.myProject.demo.Services;

import com.myProject.demo.DTO.WalletRequest;
import com.myProject.demo.DTO.WalletResponse;
import com.myProject.demo.Models.User;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.UserRepo;
import com.myProject.demo.Repositories.WalletRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {
    @Autowired
    private WalletRepo walletrepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userrepo;

    Logger log = LoggerFactory.getLogger(WalletService.class);



    @CacheEvict(value="wallets", allEntries=true)
    public void AddWallet(WalletRequest walletRequest)
    {
        log.info("searching on user " );
        User user=userrepo.findById(walletRequest.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("the user {} is found ",user.getUsername() );
      Wallet wallet=  modelMapper.map(walletRequest, Wallet.class);
      wallet.setUser(user);
      walletrepo.save(wallet);

        System.out.println("Wallet Added Successfully");

    }
    @CachePut(value="wallets",key="#id")
    public WalletResponse updateWallet(WalletRequest walletRequest,Long id)
    {
        Wallet walletfromdb=walletrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        User userfromdb=userrepo.findById(walletRequest.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        walletfromdb.setBalance(walletRequest.getBalance());
        walletfromdb.setCurrency(walletRequest.getCurrency());
        walletfromdb.setUser(userfromdb);
        walletfromdb.setUpdatedAt(LocalDateTime.now());
        walletrepo.save(walletfromdb);
        return modelMapper.map(walletfromdb, WalletResponse.class);
    }
    @Cacheable(value="wallets",key="#id")
    public WalletResponse getWalletById(Long id)
    {
        return modelMapper.map(walletrepo.findById(id).get(), WalletResponse.class);
    }
    @Cacheable("walletsList")
    public List<WalletResponse> getallWallets()
    {
        return walletrepo.findAllWallets();
    }

    @Caching(evict = {
            @CacheEvict(value = "wallets", key = "#id"),
            @CacheEvict(value = "walletsList", allEntries = true)
    })
    public void deleteWalletById(Long id)
    {
        walletrepo.deleteById(id);
        System.out.println("Wallet Deleted Successfully");
    }


}
