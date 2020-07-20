package com.nab.microservices.product.service;

import com.nab.microservices.product.dto.PhoneCardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductService {
    @GetMapping(
            value    = "/phone/card/{orderId}")
    PhoneCardDto getPhoneCard(@PathVariable  String orderId);
}
