package com.nab.microservices.core.phone.service.api;


import com.nab.microservices.core.phone.dto.AuthenticationDto;
import com.nab.microservices.core.phone.dto.VoucherDto;
import com.nab.microservices.core.phone.dto.VoucherOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface VoucherService {
    @PostMapping("/voucher/buy")
    ResponseEntity<VoucherDto> buyVoucher(@Valid  @RequestBody VoucherOrderDto voucherOrderDto);

    @GetMapping("/voucher/{orderId}")
    ResponseEntity<VoucherDto> getVoucher(@PathVariable String orderId);

    @PostMapping("/voucher/all")
    ResponseEntity<List<VoucherDto>> getAllVouchersWithAuth(@Valid  @RequestBody AuthenticationDto authenticationDto);
}
