package dev.lydtech.dispatch.service;

import dev.lydtech.dispatch.message.OrderCreated;
import dev.lydtech.dispatch.message.OrderDispatched;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DispatcherService {

    private static final String ORDER_DISPATCHED_TOPIC = "order.dispatched";

    private final KafkaTemplate<String, Object> producer;

    public void process(OrderCreated payload) throws ExecutionException, InterruptedException {
        var orderDispatched = OrderDispatched.builder()
                .orderId(payload.getOrderId())
                .build();

        producer.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();
    }
}
