package com.nab.microservices.product.enums;

public enum MockProcessSpeed {
    fast, // Get Voucher code immediately
    slow, // Get Voucher code after 30 seconds
    error // 3rd party error
}
