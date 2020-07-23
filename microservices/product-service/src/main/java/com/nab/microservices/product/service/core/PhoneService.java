package com.nab.microservices.product.service.core;

import com.nab.microservices.product.dto.VoucherDto;
import com.nab.microservices.product.dto.VoucherOrderDto;

public interface PhoneService {
    VoucherDto buyVoucher(VoucherOrderDto voucherOrderDto);
    VoucherDto getVoucher(String orderId);
}
