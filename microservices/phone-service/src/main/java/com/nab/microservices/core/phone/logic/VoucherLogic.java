package com.nab.microservices.core.phone.logic;


import com.nab.microservices.core.phone.dto.*;
import com.nab.microservices.core.phone.enums.VoucherOrderStatus;
import com.nab.microservices.core.phone.exceptions.InvalidInputException;
import com.nab.microservices.core.phone.persistence.PhoneVerification;
import com.nab.microservices.core.phone.persistence.PhoneVerificationRepository;
import com.nab.microservices.core.phone.persistence.Voucher;
import com.nab.microservices.core.phone.persistence.VoucherRepository;
import com.nab.microservices.core.phone.service.jms.VoucherOrderCreationNotification;
import com.nab.microservices.core.phone.service.jms.SMSCreationNotification;
import com.nab.microservices.core.phone.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VoucherLogic {
    private static final Logger LOG = LoggerFactory.getLogger(VoucherLogic.class);
    private VoucherRepository voucherRepository;
    private VoucherOrderCreationNotification orderCreationNotification;
    private SMSCreationNotification smsCreationNotification;
    private PhoneVerificationRepository phoneVerificationRepository;
    private static final int MAX_WAIT_TIME = 30;
    @Value("${application.mockSpeed}")
    private boolean mockSpeed;


    public VoucherLogic(VoucherRepository voucherRepository,
                        VoucherOrderCreationNotification orderCreationNotification,
                        SMSCreationNotification smsCreationNotification,
                        PhoneVerificationRepository phoneVerificationRepository){
        this.voucherRepository = voucherRepository;
        this.orderCreationNotification = orderCreationNotification;
        this.smsCreationNotification = smsCreationNotification;
        this.phoneVerificationRepository = phoneVerificationRepository;
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

    public void updateVoucherCode(VoucherOrderSQSDto voucherOrderSQSDto)  throws IOException  {
        String orderId = voucherOrderSQSDto.getOrderId();
        Optional<Voucher> voucherOptional = voucherRepository.findFirstByOrderId(orderId);
        if( !voucherOptional.isPresent() ) {
            LOG.error("Can not find voucher information by orderID: " + orderId);
            throw new InvalidInputException("Can not find Voucher Order by OrderId " + orderId);
        }

        Voucher voucher = voucherOptional.get();
        voucher.setVoucherCode(voucherOrderSQSDto.getVoucherCode());
        voucher.setStatus(voucherOrderSQSDto.getStatus());

        voucherRepository.save(voucher);

        long periodTimeInSecond = TimeUtil.differenceInSecondWithCurrentTime(voucher.getTimestamp());
        if( voucher.getStatus() == VoucherOrderStatus.finish && periodTimeInSecond > MAX_WAIT_TIME ) {
            sendVoucherCodeSMS(voucher);
        }
    }

    private void sendVoucherCodeSMS(Voucher voucher) throws IOException {
        SmsSQSDto smsSQSDto = new SmsSQSDto();
        smsSQSDto.setMessage("Your Voucher Code: " + voucher.getVoucherCode());
        smsSQSDto.setPhoneNumber(voucher.getPhoneNumber());
        this.smsCreationNotification.notificationRequest(smsSQSDto);
    }

    public VoucherDto getVoucher(String orderId) {
        if ( StringUtils.isBlank(orderId) ) return null;

        Optional<Voucher> voucherOptional = voucherRepository.findFirstByOrderId(orderId);
        if( !voucherOptional.isPresent() ) {
            LOG.error("Can not find voucher information by orderID: " + orderId);
            VoucherDto voucherDto = new VoucherDto();
            voucherDto.setOrderId(orderId);
            voucherDto.setMessage("Can not find voucher information by orderID: " + orderId);
            return voucherDto;
        }

        Voucher voucher = voucherOptional.get();
        VoucherDto voucherDto = VoucherDto.fromVoucher(voucher);


        if( voucher.getStatus() == VoucherOrderStatus.processing ) {
            long periodTimeInSecond = TimeUtil.differenceInSecondWithCurrentTime(voucher.getTimestamp());

            if ( periodTimeInSecond <= MAX_WAIT_TIME ) {
                voucherDto.setMessage("Voucher order request is being processed within 30 seconds");
            } else {
                voucherDto.setMessage("It take long time to process you order. We will send SMS to you when it's finish");
            }

        }


        return voucherDto;
    }

    public List<VoucherDto> getAllVouchersWithAuth(AuthenticationDto authenticationDto){
        Optional<PhoneVerification> phoneVerificationOptional = this.phoneVerificationRepository.findFirstByPhoneNumberAndCode(authenticationDto.getPhoneNumber(), authenticationDto.getCode());
        if ( !phoneVerificationOptional.isPresent() ) {
            return new ArrayList<>();
        }

        resetVerificationCode(phoneVerificationOptional.get());

        Optional<List<Voucher>> allVoucherOptional = voucherRepository.findAllByPhoneNumber(authenticationDto.getPhoneNumber());

        if( allVoucherOptional.isPresent() ) {
            List<VoucherDto> vouchers = allVoucherOptional.get().stream().map(VoucherDto::fromVoucher).collect(Collectors.toList());
            return vouchers;
        } else {
            return new ArrayList<>();
        }
    }

    private void resetVerificationCode(PhoneVerification phoneVerification){
        phoneVerification.setCode("");
        phoneVerificationRepository.save(phoneVerification);
    }
}
