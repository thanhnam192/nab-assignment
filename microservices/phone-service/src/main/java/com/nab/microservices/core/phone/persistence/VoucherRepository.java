package com.nab.microservices.core.phone.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends CrudRepository<Voucher, Integer> {
    Optional<Voucher> findFirstByOrderId(String orderId);
    Optional<List<Voucher>> findAllByPhoneNumber(String phoneNumber);
}
