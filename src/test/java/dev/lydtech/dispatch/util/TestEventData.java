package dev.lydtech.dispatch.util;

import dev.lydtech.dispatch.message.OrderCreated;

import java.util.UUID;

public class TestEventData {
    public static OrderCreated createOrderCreatedEvent(String item){
        return OrderCreated.builder()
                .orderId(UUID.randomUUID())
                .item(item)
                .build();
    }
}
