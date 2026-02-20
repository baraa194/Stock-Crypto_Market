package com.myProject.demo.Listeners;

import com.myProject.demo.Events.TradeExecutedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DebugEventListener {

    @EventListener
    public void handleAllEvents(Object event) {
        if (event instanceof TradeExecutedEvent) {
            TradeExecutedEvent tradeEvent = (TradeExecutedEvent) event;
            System.out.println("\n🔍 DEBUG EVENT LISTENER - TRADE EVENT:");
            System.out.println("   Trade ID: " + tradeEvent.getTradeId());
            System.out.println("   Event Class: " + event.getClass().getName());
            System.out.println("   Timestamp: " + tradeEvent.getExecutedAt());
        } else {
            System.out.println("\n🔍 DEBUG EVENT LISTENER - Other Event:");
            System.out.println("   Event Type: " + event.getClass().getName());
        }
    }
}