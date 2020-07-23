package com.nab.microservices.core.phone.service.jms;

import com.nab.microservices.core.phone.dto.VoucherOrderSQSDto;
import com.nab.microservices.core.phone.logic.VoucherLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.io.IOException;

@Component
public class VoucherOrderResultJmsListenerService {

    private static final Logger LOG = LoggerFactory.getLogger(VoucherOrderResultJmsListenerService.class);

    @Autowired
    private VoucherLogic voucherLogic;

    @JmsListener(destination = "${application.queue.result}")
    public void updateVoucher(String responseJSON) throws Exception {
        LOG.info("Received Order Result : " + responseJSON);
        try {
            VoucherOrderSQSDto voucherOrderSQSDto = VoucherOrderSQSDto.fromJSON(responseJSON);
            LOG.info(voucherOrderSQSDto.getMessage());
            voucherLogic.updateVoucherCode(voucherOrderSQSDto);
        } catch (IOException ex) {
            LOG.error("Encountered error while parsing message.",ex);
            throw new JMSException("Encountered error while parsing message.");
        }
    }
}
