package com.myProject.demo.Services;

import com.myProject.demo.DTO.LedgerEntryRequest;
import com.myProject.demo.DTO.LedgerEntryResponse;
import com.myProject.demo.Models.LedgerEntry;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.LedgerRepo;
import com.myProject.demo.Repositories.WalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LedgerService {
    private final LedgerRepo ledgerRepo;
    private final WalletRepo walletRepo;

    public LedgerEntry createEntry(LedgerEntryRequest request) {
        Wallet wallet = walletRepo.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        LedgerEntry entry = new LedgerEntry();

        entry.setWallet(wallet);
        entry.setType(request.getType());
        entry.setAmount(request.getAmount());
        entry.setBalanceBefore(request.getBalanceBefore());
        entry.setBalanceAfter(request.getBalanceAfter());
        entry.setReferenceType(request.getReferenceType());
        entry.setReferenceId(request.getReferenceId());
        entry.setDescription(request.getDescription());

        return ledgerRepo.save(entry);
    }

    public List<LedgerEntryResponse> getWalletLedger(Long walletId) {

        return ledgerRepo
                .findByWalletIdOrderByCreatedAtDesc(walletId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    private LedgerEntryResponse mapToResponse(LedgerEntry entry) {

        LedgerEntryResponse response = new LedgerEntryResponse();

        response.setId(entry.getId());
        response.setWalletId(entry.getWallet().getId());
        response.setType(entry.getType());
        response.setAmount(entry.getAmount());
        response.setBalanceBefore(entry.getBalanceBefore());
        response.setBalanceAfter(entry.getBalanceAfter());
        response.setReferenceType(entry.getReferenceType());
        response.setReferenceId(entry.getReferenceId());
        response.setDescription(entry.getDescription());
        response.setCreatedAt(entry.getCreatedAt());

        return response;
    }





}
