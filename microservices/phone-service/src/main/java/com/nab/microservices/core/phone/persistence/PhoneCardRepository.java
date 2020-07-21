package com.nab.microservices.core.phone.persistence;

import org.springframework.data.repository.CrudRepository;

public interface PhoneCardRepository extends CrudRepository<PhoneCard, Integer> {
}
