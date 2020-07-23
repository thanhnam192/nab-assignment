package com.nab.microservices.core.phone.enums;

public enum MockProcessSpeed {
    fast, // Get voucher code immediately
    slow, // Get voucher code after 30 seconds
    error // 3rd party error
}
