package com.nab.microservices.product.controller;

import com.nab.microservices.product.dto.PhoneCardDto;
import com.nab.microservices.product.service.ProductIntegration;
import com.nab.microservices.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController implements ProductService {
    public ProductIntegration productIntegration;

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);


    public ProductController(ProductIntegration productIntegration){
        this.productIntegration = productIntegration;
    }

    @Override
    public PhoneCardDto getPhoneCard(String orderId) {
        PhoneCardDto phoneCard = productIntegration.getPhoneCard(orderId);
        return  phoneCard;
    }

}
