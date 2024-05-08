package com.example.kafka.consumer;

import com.example.kafka.utils.EncryptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class KafkaMessageConsumer {

    private static final Logger LOGGER = LogManager.getLogger(KafkaMessageConsumer.class);

    private final ConcurrentLinkedQueue<byte[]> messageQueue = new ConcurrentLinkedQueue<>();


    @KafkaListener(topics = "micro_topic",groupId = "group1")
    public void consumeMicro(Object encryptedMessage) throws Exception {
        LOGGER.info("new message consumed at "+ LocalDateTime.now()+encryptedMessage);
//         Decrypt the encrypted message
        byte[] decryptedMessage = EncryptionUtil.decrypt((byte[]) encryptedMessage);

        // Add the decrypted message to the queue for further processing
        messageQueue.add(decryptedMessage);
    }

    public Boolean isQueEmpty(){
        return messageQueue.isEmpty();
    }

    // Method to retrieve and process the next message from the queue
    public byte[] getNextMicroMessage() {
        return messageQueue.peek();
    }

}

