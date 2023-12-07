package dev.lydtech.dispatch.service;

import dev.lydtech.dispatch.message.DispatchPreparing;
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
    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";


    private final KafkaTemplate<String, Object> producer;

    public void process(OrderCreated payload) throws ExecutionException, InterruptedException {
        var orderDispatched = OrderDispatched.builder()
                .orderId(payload.getOrderId())
                .build();

        producer.send(ORDER_DISPATCHED_TOPIC, orderDispatched).get();

        var dispatchPreparing = DispatchPreparing.builder()
                .orderId(payload.getOrderId())
                .build();

        producer.send(DISPATCH_TRACKING_TOPIC, dispatchPreparing).get();
    }
}
