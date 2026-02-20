package com.myProject.demo.Services;


import com.myProject.demo.Models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotificationService {

@Autowired
private SimpMessagingTemplate MessagingTemplate;

public void sendNotification(String Username,String message)
{
    // send notification to specific user
    MessagingTemplate.convertAndSend("/topic/trades" + Username, message);
}
public void sendWalletupdate(String Username, String message)
{

MessagingTemplate.convertAndSend("/topic/walletupdate" + Username,message );

}



}
