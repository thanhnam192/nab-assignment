package com.nab.microservices.core.phone.logic;


import com.nab.microservices.core.phone.dto.VoucherDto;
import com.nab.microservices.core.phone.dto.VoucherOrderDto;
import com.nab.microservices.core.phone.dto.VoucherOrderSQSDto;
import com.nab.microservices.core.phone.dto.SmsSQSDto;
import com.nab.microservices.core.phone.enums.VoucherOrderStatus;
import com.nab.microservices.core.phone.persistence.Voucher;
import com.nab.microservices.core.phone.persistence.VoucherRepository;
import com.nab.microservices.core.phone.service.jms.VoucherOrderCreationNotification;
import com.nab.microservices.core.phone.service.jms.SMSCreationNotification;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
public class VoucherLogic {
    private static final Logger LOG = LoggerFactory.getLogger(VoucherLogic.class);
    private VoucherRepository voucherRepository;
    private VoucherOrderCreationNotification orderCreationNotification;
    private SMSCreationNotification smsCreationNotification;
    @Value("${application.mockSpeed}")
    private boolean mockSpeed;


    public VoucherLogic(VoucherRepository voucherRepository, VoucherOrderCreationNotification orderCreationNotification, SMSCreationNotification smsCreationNotification){
        this.voucherRepository = voucherRepository;
        this.orderCreationNotification = orderCreationNotification;
        this.smsCreationNotification = smsCreationNotification;
    }

    public VoucherDto createVoucherOrder(VoucherOrderDto voucherOrderDto) throws IOException {
        Voucher voucher = new Voucher();
        voucher.setPhoneNumber(voucherOrderDto.getPhoneNumber());
        voucher.setMobileNetwork(voucherOrderDto.getMobileNetwork());
        voucher.setPrice(voucherOrderDto.getPrice());

        String orderId = UUID.randomUUID().toString();
        voucher.setOrderId(orderId);

        voucher.setTimestamp(new Timestamp(System.currentTimeMillis()));

        voucher.setStatus(VoucherOrderStatus.processing);

        voucherRepository.save(voucher);
        LOG.info("Created Voucher Order");
        LOG.info("Send Voucher Order to queue");
        VoucherOrderSQSDto voucherOrderSQSDto =  VoucherOrderSQSDto.from(voucher);

        if( mockSpeed ) {
            voucherOrderSQSDto.setMockSpeed(voucherOrderDto.getMockSpeed());
        }

        this.orderCreationNotification.notificationRequest(voucherOrderSQSDto);

        return VoucherDto.fromVoucher(voucher);

    }

    public void updateVoucherCode(VoucherOrderSQSDto voucherOrderSQSDto) throws Exception {
        String orderId = voucherOrderSQSDto.getOrderId();
        Optional<Voucher> voucherOptional = voucherRepository.findFirstByOrderId(orderId);
        if( !voucherOptional.isPresent() ) {
            LOG.error("Can not find voucher information by orderID: " + orderId);
            throw new Exception("Can not find Voucher Order by OrderId " + orderId);
        }

        Voucher voucher = voucherOptional.get();
        voucher.setVoucherCode(voucherOrderSQSDto.getVoucherCode());
        voucher.setStatus(voucherOrderSQSDto.getStatus());

        voucherRepository.save(voucher);

        if( voucher.getStatus() == VoucherOrderStatus.finish ) {
            sendVoucherCodeSMS(voucher);
        }
    }

    private void sendVoucherCodeSMS(Voucher voucher) throws IOException {
        SmsSQSDto smsSQSDto = new SmsSQSDto();
        smsSQSDto.setMessage("Your Voucher Code: " + voucher.getVoucherCode());
        smsSQSDto.setPhoneNumber(voucher.getPhoneNumber());
        this.smsCreationNotification.notificationRequest(smsSQSDto);
    }

    public VoucherDto getVoucher(String orderId) throws Exception {
        if ( StringUtils.isBlank(orderId) ) return null;

        Optional<Voucher> voucherOptional = voucherRepository.findFirstByOrderId(orderId);
        if( !voucherOptional.isPresent() ) {
            LOG.error("Can not find voucher information by orderID: " + orderId);
            throw new Exception("Can not find voucher by OrderId " + orderId);
        }

        Voucher voucher = voucherOptional.get();
        VoucherDto voucherDto = VoucherDto.fromVoucher(voucher);

        if( voucher.getStatus() == VoucherOrderStatus.processing ) {
            voucherDto.setMessage("Your voucher is processing. We will send sms to you when it's finish");
        }


        return voucherDto;
    }
}
