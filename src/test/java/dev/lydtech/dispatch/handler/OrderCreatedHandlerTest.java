package dev.lydtech.dispatch.handler;

import dev.lydtech.dispatch.service.DispatcherService;
import dev.lydtech.dispatch.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedHandlerTest {

    private OrderCreatedHandler handler;

    private DispatcherService service;

    @BeforeEach
    void setUp(){
        service = Mockito.mock(DispatcherService.class);
        handler = new OrderCreatedHandler(service);
    }

    @Test
    void listen_Success() throws Exception{
        var event = TestEventData.createOrderCreatedEvent("test item");
        handler.listen(event);
        Mockito.verify(service, Mockito.times(1)).process(event);
    }

    @Test
    void listen_ThrowsException() throws Exception{
        var event = TestEventData.createOrderCreatedEvent("test item");
        Mockito.doThrow(new RuntimeException("Processing failed.")).when(service).process(event);
        handler.listen(event);
        Mockito.verify(service, Mockito.times(1)).process(event);
    }
}