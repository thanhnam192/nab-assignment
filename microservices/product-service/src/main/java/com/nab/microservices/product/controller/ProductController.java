package com.nab.microservices.product.controller;

import com.nab.microservices.product.dto.VoucherDto;
import com.nab.microservices.product.dto.VoucherOrderDto;
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
    public ResponseEntity<VoucherDto> buyVoucher(VoucherOrderDto voucherOrderDto) {
        LOG.info("Start to create voucher order");
        if( !mockSpeed ) {
            voucherOrderDto.setMockSpeed(MockProcessSpeed.fast);
        }
        VoucherDto voucherDto = this.productIntegration.buyVoucher(voucherOrderDto);
        return  ResponseEntity.ok().body(voucherDto);
    }

    @Override
    public ResponseEntity<VoucherDto> getVoucher(String orderId) {
        VoucherDto voucherDto = productIntegration.getVoucher(orderId);
        return  ResponseEntity.ok().body(voucherDto);
    }



}
