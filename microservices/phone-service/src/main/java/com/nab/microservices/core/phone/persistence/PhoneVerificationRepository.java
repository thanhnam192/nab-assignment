package com.nab.microservices.core.phone.persistence;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface PhoneVerificationRepository extends CrudRepository<PhoneVerification, Integer> {
    Optional<PhoneVerification> findFirstByPhoneNumber(String phoneNumber);
    Optional<PhoneVerification> findFirstByPhoneNumberAndCode(String phoneNumber, String code);

}
