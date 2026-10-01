package com.myProject.demo.Services;

import com.myProject.demo.DTO.LedgerEntryRequest;
import com.myProject.demo.DTO.WalletRequest;
import com.myProject.demo.DTO.WalletResponse;
import com.myProject.demo.DTO.WalletTransactionRequest;
import com.myProject.demo.Enums.LedgerType;
import com.myProject.demo.Enums.ReferenceType;
import com.myProject.demo.Exceptions.InsufficientFundsException;
import com.myProject.demo.Exceptions.UserNotFoundException;
import com.myProject.demo.Models.User;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.UserRepo;
import com.myProject.demo.Repositories.WalletRepo;
import jakarta.transaction.Transactional;
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

import java.math.BigDecimal;
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
    @Autowired
    private LedgerService ledgerService;

    Logger log = LoggerFactory.getLogger(WalletService.class);



   @CacheEvict(value="wallets", allEntries=true)
    public void AddWallet(WalletRequest walletRequest)
    {
        log.info("searching on user " );
        User user=userrepo.findById(walletRequest.getUser_id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        log.info("the user {} is found ",user.getUsername() );
      Wallet wallet=  modelMapper.map(walletRequest, Wallet.class);
      wallet.setUser(user);
      walletrepo.save(wallet);


    }
    @CachePut(value="wallets",key="#id")
    public WalletResponse updateWallet(WalletRequest walletRequest,Long id)
    {
        Wallet walletfromdb=walletrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        User userfromdb=userrepo.findById(walletRequest.getUser_id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        walletfromdb.setBalance(walletRequest.getBalance());
        walletfromdb.setCurrency(walletRequest.getCurrency());
        walletfromdb.setUser(userfromdb);
        walletfromdb.setUpdatedAt(LocalDateTime.now());
        walletrepo.save(walletfromdb);
        return modelMapper.map(walletfromdb, WalletResponse.class);
    }
    // withdrawl service
    @Transactional
    public WalletResponse withdraw(WalletTransactionRequest request) {

        Wallet wallet = walletrepo.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        BigDecimal availableBalance =
                wallet.getBalance().subtract(wallet.getReservedBalance());

        if (availableBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient available balance");
        }

        BigDecimal balanceBefore = wallet.getBalance();

        wallet.setBalance(
                wallet.getBalance().subtract(request.getAmount())
        );

        wallet.setUpdatedAt(LocalDateTime.now());

        walletrepo.save(wallet);

        LedgerEntryRequest ledgerRequest =
                new LedgerEntryRequest();

        ledgerRequest.setWalletId(wallet.getId());
        ledgerRequest.setType(LedgerType.WITHDRAWAL);


        ledgerRequest.setAmount(request.getAmount().negate());

        ledgerRequest.setBalanceBefore(balanceBefore);
        ledgerRequest.setBalanceAfter(wallet.getBalance());

        ledgerRequest.setReferenceType(ReferenceType.WALLET);
        ledgerRequest.setReferenceId(wallet.getId());
        ledgerRequest.setDescription("Wallet withdrawal");

        ledgerService.createEntry(ledgerRequest);

        return modelMapper.map(wallet, WalletResponse.class);
    }

    @Transactional
    public WalletResponse deposit(WalletTransactionRequest request) {

        Wallet wallet = walletrepo.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        BigDecimal balanceBefore = wallet.getBalance();

        wallet.setBalance(
                wallet.getBalance().add(request.getAmount())
        );

        wallet.setUpdatedAt(LocalDateTime.now());

        walletrepo.save(wallet);

        LedgerEntryRequest ledgerRequest =
                new LedgerEntryRequest();

        ledgerRequest.setWalletId(wallet.getId());
        ledgerRequest.setType(LedgerType.DEPOSIT);
        ledgerRequest.setAmount(request.getAmount());
        ledgerRequest.setBalanceBefore(balanceBefore);
        ledgerRequest.setBalanceAfter(wallet.getBalance());
        ledgerRequest.setReferenceType(ReferenceType.WALLET);
        ledgerRequest.setReferenceId(wallet.getId());
        ledgerRequest.setDescription("Wallet deposit");

        ledgerService.createEntry(ledgerRequest);

        return modelMapper.map(wallet, WalletResponse.class);
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
        log.info("Wallet with id ${} Deleted Successfully",id);
    }


}
