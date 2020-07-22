package com.nab.microservices.product.controller;

import com.nab.microservices.product.dto.PhoneCardDto;
import com.nab.microservices.product.dto.PhoneCardOrderDto;
import com.nab.microservices.product.enums.MockProcessSpeed;
import com.nab.microservices.product.service.ProductIntegration;
import com.nab.microservices.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
    private ProductIntegration productIntegration;
    @Value("${application.mockSpeed}")
    private boolean mockSpeed;




    public ProductController(ProductIntegration productIntegration){
        this.productIntegration = productIntegration;
    }

    @Override
    public ResponseEntity<PhoneCardDto> buyPhoneCard(PhoneCardOrderDto phoneCardOrderDto) {
        LOG.info("Start to create phone card order");
        if( !mockSpeed ) {
            phoneCardOrderDto.setMockSpeed(MockProcessSpeed.fast);
        }
        PhoneCardDto phoneCardDto = this.productIntegration.buyPhoneCard(phoneCardOrderDto);
        return  ResponseEntity.ok().body(phoneCardDto);
    }

    @Override
    public ResponseEntity<PhoneCardDto> getPhoneCard(String orderId) {
        PhoneCardDto phoneCard = productIntegration.getPhoneCard(orderId);
        return  ResponseEntity.ok().body(phoneCard);
    }



}
