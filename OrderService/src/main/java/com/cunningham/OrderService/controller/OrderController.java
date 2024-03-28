package com.cunningham.OrderService.controller;

import com.cunningham.OrderService.entity.Order;
import com.cunningham.OrderService.service.BookServiceClient;
import com.cunningham.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final BookServiceClient bookServiceClient;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // First, check if the book exists in BookService
        boolean book = bookServiceClient.getBookByTitle(order.getBookTitle());

        if (!book) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book Not Found");
        }

        // If the book exists, proceed to create the order
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}