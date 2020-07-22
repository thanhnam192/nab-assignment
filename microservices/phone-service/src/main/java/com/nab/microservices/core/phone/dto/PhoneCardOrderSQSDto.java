package com.nab.microservices.core.phone.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;

import java.io.IOException;
import java.math.BigDecimal;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneCardOrderSQSDto {
    private String phoneNumber;
    private String orderId;
    private String mobileNetwork;
    private BigDecimal price;
    private String cardNumber;
    private PhoneCardOrderStatus status;
    private String message;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public PhoneCardOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PhoneCardOrderStatus status) {
        this.status = status;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static PhoneCardOrderSQSDto fromJSON(String json)
            throws JsonProcessingException, IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(json, PhoneCardOrderSQSDto.class);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(this);
    }
}
