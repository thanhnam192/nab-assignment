package com.nab.microservices.core.phone.persistence;


import com.nab.microservices.core.phone.enums.PhoneCardOrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "phone_card")
public class PhoneCard {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "card_number")
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private PhoneCardOrderStatus status;
    private Timestamp timestamp;

    @Column(name = "mobile_network", nullable = false)
    private String mobileNetwork;

    @Column(nullable = false)
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PhoneCardOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PhoneCardOrderStatus status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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
}
