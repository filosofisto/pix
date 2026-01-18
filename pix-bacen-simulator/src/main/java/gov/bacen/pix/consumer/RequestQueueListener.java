package gov.bacen.pix.consumer;

import gov.bacen.pix.domain.pac008.Pac008Document;
import gov.bacen.pix.exception.SystemException;
import gov.bacen.pix.service.PixService;
import gov.bacen.pix.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * TODO: there is a conflict between javax.jms and jakarta.jms on Spring 4, a trick problem so this class aren't functional now
 */
//@Component
//@Slf4j
//@RequiredArgsConstructor
public class RequestQueueListener {

//    private final PixService pixService;

//    @JmsListener(destination = "${ibm.mq.request.queue-name}")
//    public void onMessage(String payload) {
//        log.info("Received request queue payload: {}", payload);
//
//        try {
//            Pac008Document pac008Document = XmlUtil.fromXml(payload);
//
//            pixService.processPix(pac008Document);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new SystemException(e);
//        }
//    }
}

