package dev.lydtech.dispatch.service;

import dev.lydtech.dispatch.message.OrderDispatched;
import dev.lydtech.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DispatcherServiceTest {

    private DispatcherService service;
    private KafkaTemplate<String, Object> producer;

    @BeforeEach
    void setUp() {
        producer = Mockito.mock(KafkaTemplate.class);
        service = new DispatcherService(producer);
    }

    @Test
    void process_success() throws Exception {
        when(producer.send(anyString(), any(OrderDispatched.class))).thenReturn(mock(CompletableFuture.class));

        service.process(TestEventData.createOrderCreatedEvent("test item"));

        verify(producer, times(1)).send(eq("order.dispatched"), any(OrderDispatched.class));
    }

    @Test
    void process_throwsException() throws Exception {
        var order = TestEventData.createOrderCreatedEvent("item");
        doThrow(new RuntimeException("Processing failed")).when(producer).send(eq("order.dispatched"), any(OrderDispatched.class));

        var exception = assertThrows(RuntimeException.class, () -> service.process(order));

        verify(producer, times(1)).send(eq("order.dispatched"), any(OrderDispatched.class));
        assertEquals(exception.getMessage(), "Processing failed");
    }
}