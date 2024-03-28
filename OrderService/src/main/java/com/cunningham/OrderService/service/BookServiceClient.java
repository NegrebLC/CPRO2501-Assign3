package com.cunningham.OrderService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookServiceClient {

    private final RestTemplate restTemplate;

    private final String bookServiceUrl;

    @Autowired
    public BookServiceClient(RestTemplate restTemplate, @Value("${bookservice.url}") String bookServiceUrl) {
        this.restTemplate = restTemplate;
        this.bookServiceUrl = bookServiceUrl;
    }

    public boolean getBookByTitle(String title) {
        Boolean exists = restTemplate.getForObject(bookServiceUrl + "/search?title=" + title, Boolean.class);
        return exists != null && exists;
    }
}