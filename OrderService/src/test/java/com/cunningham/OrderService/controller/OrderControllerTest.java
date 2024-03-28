package com.cunningham.OrderService.controller;

import com.cunningham.OrderService.entity.Order;
import com.cunningham.OrderService.service.BookServiceClient;
import com.cunningham.OrderService.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private BookServiceClient bookServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrderBookExists() throws Exception {
        when(bookServiceClient.getBookByTitle(any(String.class))).thenReturn(true);

        Order order = new Order(null, "Sample Book", 1, "customer@example.com");
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated());
    }

    @Test
    void createOrderBookNotFound() throws Exception {
        when(bookServiceClient.getBookByTitle(any(String.class))).thenReturn(false);

        Order order = new Order(null, "Nonexistent Book", 1, "customer@example.com");
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound());
    }
}