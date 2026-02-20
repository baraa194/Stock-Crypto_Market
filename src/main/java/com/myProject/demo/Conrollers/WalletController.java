package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.WalletRequest;
import com.myProject.demo.DTO.WalletResponse;
import com.myProject.demo.Services.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;
    Logger log = LoggerFactory.getLogger(WalletController.class);


    @PostMapping("/add")
    public ResponseEntity<String> addWallet(@RequestBody WalletRequest walletRequest) {
        System.out.println("HELLO");

        walletService.AddWallet(walletRequest);
        return ResponseEntity.ok("Wallet added successfully");
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<WalletResponse> updateWallet(
            @RequestBody WalletRequest walletRequest,
            @PathVariable Long id) {
        WalletResponse updatedWallet = walletService.updateWallet(walletRequest, id);
        return ResponseEntity.ok(updatedWallet);
    }


    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable Long id) {
        WalletResponse wallet = walletService.getWalletById(id);
        return ResponseEntity.ok(wallet);
    }


    @GetMapping("/all")
    public ResponseEntity<List<WalletResponse>> getAllWallets() {
        List<WalletResponse> wallets = walletService.getallWallets();
        return ResponseEntity.ok(wallets);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWallet(@PathVariable Long id) {
        walletService.deleteWalletById(id);
        return ResponseEntity.ok("Wallet deleted successfully");
    }
}
