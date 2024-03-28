package com.cunningham.OrderService.service;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.cunningham.OrderService.entity.Order;
import com.cunningham.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderSuccess() {
        Order order = new Order(1L, "Sample Book", 1, "customer@example.com");
        when(orderRepository.save(any(Order.class))).thenReturn(new Order(1L, "Sample Book", 1, "customer@example.com"));

        orderService.createOrder(order);

        // Verify that the repository's save method was called
        verify(orderRepository).save(any(Order.class));
    }
}
