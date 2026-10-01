package com.myProject.demo.Listeners;

import com.myProject.demo.Events.TradeExecutedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebugEventListener {

    @EventListener
    public void handleAllEvents(Object event) {
        if (event instanceof TradeExecutedEvent) {
            TradeExecutedEvent tradeEvent = (TradeExecutedEvent) event;
            log.info(" Run Event listner with Trade ID: ${} Eventclass: ${}  "
                    ,tradeEvent.getTradeId(),event.getClass().getName());

        } else {
            log.info("   Event Type: ${}" , event.getClass().getName());
        }
    }
}