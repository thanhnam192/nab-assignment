package com.nab.microservices.core.phone.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PhoneVerificationPersistenceTest {
    @Autowired
    private PhoneVerificationRepository repository;

    private PhoneVerification savedEntity;

    private static final String PHONE_NUMBER = "+84986329076";
    private static final String CODE = "abcd";
    private static final Timestamp TIMESTAMP = new Timestamp(System.currentTimeMillis());

    @Before
    public void setupDb() {
        repository.deleteAll();

        PhoneVerification phoneVerification = new PhoneVerification();

        phoneVerification.setPhoneNumber(PHONE_NUMBER);
        phoneVerification.setCode(CODE);
        phoneVerification.setTimestamp(TIMESTAMP);

        savedEntity = repository.save(phoneVerification);

    }

    @Test
    public void findByPhoneNumber() {
        Optional<PhoneVerification> foundEntity = repository.findFirstByPhoneNumber(savedEntity.getPhoneNumber());
        assertTrue(foundEntity.isPresent());
        assertEqualsPhoneVerification(savedEntity, foundEntity.get());
    }

    @Test
    public void updateAuthCode() {
        savedEntity.setCode("56789");
        repository.save(savedEntity);

        Optional<PhoneVerification> foundEntity  = repository.findFirstByPhoneNumber(savedEntity.getPhoneNumber());
        assertTrue(foundEntity.isPresent());
        assertEquals("56789", foundEntity.get().getCode());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicateError() {
        PhoneVerification phoneVerification = new PhoneVerification();

        phoneVerification.setPhoneNumber(PHONE_NUMBER);
        phoneVerification.setCode(CODE);
        phoneVerification.setTimestamp(TIMESTAMP);

        repository.save(phoneVerification);
    }



    private void assertEqualsPhoneVerification(PhoneVerification expectedEntity, PhoneVerification actualEntity) {
        assertEquals(expectedEntity.getId(),        actualEntity.getId());
        assertEquals(expectedEntity.getPhoneNumber(), actualEntity.getPhoneNumber());
        assertEquals(expectedEntity.getCode(),  actualEntity.getCode());
        assertEquals(expectedEntity.getTimestamp(),   actualEntity.getTimestamp());
    }
}
