package com.myProject.demo.Services;

import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Exceptions.InsufficientFundsException;
import com.myProject.demo.Exceptions.InvalidTradeQuantityException;
import com.myProject.demo.Models.Order;
import com.myProject.demo.Models.PortfolioItem;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.PortfolioItemRepo;
import com.myProject.demo.Repositories.PortfolioRepo;
import com.myProject.demo.Repositories.WalletRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ReservationService {

    private final WalletRepo walletRepo;
    private final PortfolioRepo portfolioRepo;
    private final PortfolioItemRepo  portfolioItemRepo;

    public void reserveBuyFunds(Order order) {

     Wallet wallet=walletRepo.findByUserUsername(order.getUser().getUsername());
     BigDecimal totalAmount=order.getQuantity().multiply(order.getTargetPrice());
        if (wallet.getReservedBalance() == null) {
            wallet.setReservedBalance(BigDecimal.ZERO);
        }

        BigDecimal availableBalance =
                wallet.getBalance().subtract(wallet.getReservedBalance());
        if(availableBalance.compareTo(totalAmount) >= 0)
        {
            BigDecimal newReservedBalance =
                    wallet.getReservedBalance().add(totalAmount);

            wallet.setReservedBalance(newReservedBalance);
        }else{

            throw new InsufficientFundsException("No available money");
        }


   walletRepo.save(wallet);

    }

    public void reserveSellQuantity(Order order) {

        PortfolioItem portfolioItem =
                portfolioItemRepo.findByPortfolioIdAndAssetName(order.getPortfolio().getId(),
                                order.getAsset().getName())
                        .orElseThrow(() ->
                                new RuntimeException("Asset not found in portfolio")
                        );

        BigDecimal availableQuantity =
                portfolioItem.getQuantity()
                        .subtract(portfolioItem.getReservedQuantity());

        BigDecimal requiredQuantity = order.getQuantity();

        if (availableQuantity.compareTo(requiredQuantity) >= 0) {

            BigDecimal newReservedQuantity =
                    portfolioItem.getReservedQuantity()
                            .add(requiredQuantity);

            portfolioItem.setReservedQuantity(newReservedQuantity);

            portfolioItemRepo.save(portfolioItem);

        } else {
            throw new InvalidTradeQuantityException(
                    "Not enough available quantity"
            );
        }
    }

    public void releaseBuyReservation(Order order) {

        Wallet wallet =
                walletRepo.findByUserUsername(
                        order.getUser().getUsername()
                );

        BigDecimal reservedAmount =
                order.getQuantity()
                        .multiply(order.getTargetPrice());

        BigDecimal newReservedBalance =
                wallet.getReservedBalance()
                        .subtract(reservedAmount);

        wallet.setReservedBalance(newReservedBalance);

        walletRepo.save(wallet);
    }

    public void releaseSellReservation(Order order) {

        PortfolioItem portfolioItem =
                portfolioItemRepo
                        .findByPortfolioIdAndAssetName(
                                order.getPortfolio().getId(),
                                order.getAsset().getName()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Asset not found in portfolio"
                                )
                        );

        BigDecimal newReservedQuantity =
                portfolioItem.getReservedQuantity()
                        .subtract(order.getQuantity());

        portfolioItem.setReservedQuantity(newReservedQuantity);

        portfolioItemRepo.save(portfolioItem);
    }
}