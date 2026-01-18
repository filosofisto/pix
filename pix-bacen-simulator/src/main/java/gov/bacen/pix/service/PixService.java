package gov.bacen.pix.service;

import gov.bacen.pix.domain.pac002.*;
import gov.bacen.pix.domain.pac008.GroupHeader;
import gov.bacen.pix.domain.pac008.Pac008Document;
import gov.bacen.pix.publish.ResponsePublisher;
import gov.bacen.pix.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PixService {

    private final ResponsePublisher  responsePublisher;

    @Value("${ibm.mq.responseQueueName}")
    private String responseQueueName;

    public void processPix(Pac008Document pac008Document) {
        log.info("Processing Pix...");

        // Bacen logic whatever it is

        // Prepare answer -> PAC.002
        var responseDocument = preparePac002Document(pac008Document);

        // Send the answer
        sendAnswer(responseDocument);
    }

    private void sendAnswer(Pacs002Document responseDocument) {
        log.info("Generating response...");

        try {
            responsePublisher.sendMessage(XmlUtil.toXml(responseDocument), responseQueueName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private Pacs002Document preparePac002Document(Pac008Document pac008Document) {
        return new Pacs002Document(
                new FIToFIPaymentStatusReport(
                        new GroupHeader(
                                UUID.randomUUID().toString(),
                                OffsetDateTime.now(),
                                pac008Document.getFiToFiCustomerCreditTransfer().getGroupHeader().getNumberOfTransactions(),
                                pac008Document.getFiToFiCustomerCreditTransfer().getGroupHeader().getSettlementInformation()
                        ),
                        new OriginalGroupInformationAndStatus(
                                pac008Document.getFiToFiCustomerCreditTransfer().getGroupHeader().getMessageId(),
                                pac008Document.getFiToFiCustomerCreditTransfer().getGroupHeader().getMessageId(),
                                calculateGroupStatus(pac008Document)
                        ),
                        new TransactionInformationAndStatus(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                calculateTransactionStatus(pac008Document)
                        )
                )
        );
    }

    private GroupStatus calculateGroupStatus(Pac008Document pac008Document) {
        // Whatever Bacen does to aprove or not, no clue
        return GroupStatus.ACCP;
    }

    private TransactionStatus calculateTransactionStatus(Pac008Document pac008Document) {
        // Whatever Bacen does to aprove or not, no clue
        return TransactionStatus.ACSC;
    }
}
