package com.nab.microservices.core.phone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;
import com.nab.microservices.core.phone.persistence.PhoneCard;

import java.math.BigDecimal;
import java.sql.Timestamp;

@JsonInclude(Include.NON_NULL)
public class PhoneCardDto {
    private String phoneNumber;
    private String cardNumber;
    private String status;
    private String mobileNetwork;
    private BigDecimal price;
    private Timestamp createdAt;
    private String message;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static PhoneCardDto fromPhoneCard(PhoneCard phoneCard) {
        PhoneCardDto phoneCardDto = new PhoneCardDto();
        phoneCardDto.setPhoneNumber(phoneCard.getPhoneNumber());
        phoneCardDto.setCardNumber(phoneCard.getCardNumber());
        phoneCardDto.setMobileNetwork(phoneCard.getMobileNetwork());
        phoneCardDto.setPrice(phoneCard.getPrice());
        phoneCardDto.setStatus(phoneCard.getStatus().toString());
        phoneCardDto.setCreatedAt(phoneCard.getTimestamp());

        return phoneCardDto;
    }
}
