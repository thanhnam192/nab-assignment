package com.nab.microservices.core.phone.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PhoneCardRepository extends CrudRepository<PhoneCard, Integer> {
    Optional<PhoneCard> findFirstByOrderId(String orderId);
}
