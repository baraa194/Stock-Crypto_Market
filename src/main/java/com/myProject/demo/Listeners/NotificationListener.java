package com.myProject.demo.Listeners;

import com.myProject.demo.Events.TradeExecutedEvent;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.WalletRepo;
import com.myProject.demo.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Async
public class NotificationListener {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private WalletRepo walletRepo;


    @EventListener
    public void onExecutedtradeEvent(TradeExecutedEvent event) {

       notificationService.sendNotification(event.getUsername(),
               "Trade Executed On Asset "+event.getAssetName()
                       +" at price : "+ event.getPrice());


       Wallet wallet= walletRepo.findByUserUsername(event.getUsername());
        notificationService.sendWalletupdate(event.getUsername(), wallet.getBalance().toString());

        System.out.println("Your Balance : "+ wallet.getBalance());
    }



}
