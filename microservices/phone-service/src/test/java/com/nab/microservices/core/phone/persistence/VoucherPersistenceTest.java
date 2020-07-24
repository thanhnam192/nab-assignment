package com.nab.microservices.core.phone.persistence;
import com.nab.microservices.core.phone.enums.VoucherOrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class VoucherPersistenceTest {
    @Autowired
    private VoucherRepository repository;

    private Voucher savedEntity;
    private static final String MOBILE_NETWORK = "Viettel";
    private static final String PHONE_NUMBER = "+84986329076";
    private static final BigDecimal PRICE = BigDecimal.valueOf(123.0).setScale(2);
    private static final String ORDER_ID = "1234";
    private static final VoucherOrderStatus ORDER_STATUS = VoucherOrderStatus.processing;
    private static final String VOUCHER_CODE = "abcdef";
    private static final Timestamp TIMESTAMP = new Timestamp(System.currentTimeMillis());

    @Before
    public void setupDb() {
        repository.deleteAll();

        Voucher voucher = new Voucher();

        voucher.setMobileNetwork(MOBILE_NETWORK);
        voucher.setPhoneNumber(PHONE_NUMBER);
        voucher.setPrice(PRICE);
        voucher.setOrderId(ORDER_ID);
        voucher.setStatus(ORDER_STATUS);
        voucher.setVoucherCode(VOUCHER_CODE);
        voucher.setTimestamp(TIMESTAMP);

        savedEntity = repository.save(voucher);

    }

    @Test
    public void findByOrderId() {
        Optional<Voucher> foundEntity = repository.findFirstByOrderId(savedEntity.getOrderId());
        assertTrue(foundEntity.isPresent());
        assertEqualsVoucher(savedEntity, foundEntity.get());
    }

    @Test
    public void updateVoucher() {
        savedEntity.setVoucherCode("56789");
        repository.save(savedEntity);

        Optional<Voucher> foundEntity  = repository.findFirstByOrderId(savedEntity.getOrderId());
        assertTrue(foundEntity.isPresent());
        assertEquals("56789", foundEntity.get().getVoucherCode());
    }

    @Test
    public void findAllByPhoneNumer() {
        Voucher voucher = new Voucher();
        voucher.setMobileNetwork(MOBILE_NETWORK);
        voucher.setPhoneNumber(PHONE_NUMBER);
        voucher.setPrice(PRICE);
        voucher.setOrderId("test");
        voucher.setStatus(ORDER_STATUS);
        voucher.setVoucherCode(VOUCHER_CODE);
        voucher.setTimestamp(TIMESTAMP);

        repository.save(voucher);

        Optional<List<Voucher>> foundEntity  = repository.findAllByPhoneNumber(PHONE_NUMBER);
        assertTrue(foundEntity.isPresent());
        assertEquals(2, foundEntity.get().size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {

        Voucher voucher = new Voucher();
        voucher.setMobileNetwork(MOBILE_NETWORK);
        voucher.setPhoneNumber(PHONE_NUMBER);
        voucher.setPrice(PRICE);
        voucher.setOrderId(ORDER_ID);
        voucher.setStatus(ORDER_STATUS);
        voucher.setVoucherCode(VOUCHER_CODE);
        voucher.setTimestamp(TIMESTAMP);

        repository.save(voucher);

    }


    private void assertEqualsVoucher(Voucher expectedEntity, Voucher actualEntity) {
        assertEquals(expectedEntity.getId(),        actualEntity.getId());
        assertEquals(expectedEntity.getMobileNetwork(),   actualEntity.getMobileNetwork());
        assertEquals(expectedEntity.getPhoneNumber(), actualEntity.getPhoneNumber());
        assertEquals(expectedEntity.getPrice(),  actualEntity.getPrice());
        assertEquals(expectedEntity.getOrderId(),    actualEntity.getOrderId());
        assertEquals(expectedEntity.getStatus(),   actualEntity.getStatus());
        assertEquals(expectedEntity.getVoucherCode(),   actualEntity.getVoucherCode());
        assertEquals(expectedEntity.getTimestamp(),   actualEntity.getTimestamp());
    }
}
