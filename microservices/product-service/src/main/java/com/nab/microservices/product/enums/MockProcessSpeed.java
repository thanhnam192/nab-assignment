package com.nab.microservices.product.enums;

public enum MockProcessSpeed {
    fast, // Get Card Number immediately
    slow, // Get Card Number after 30 seconds
    error // 3rd party error
}
