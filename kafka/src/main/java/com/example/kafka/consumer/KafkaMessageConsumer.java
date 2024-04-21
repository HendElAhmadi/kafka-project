//package com.example.kafka.consumer;
//
//import com.example.kafka.service.FileServiceImpl;
//import com.example.kafka.utils.EncryptionUtil;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.ByteArrayDeserializer;
//import org.apache.kafka.common.serialization.ByteArraySerializer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//@Component
//public class KafkaMessageConsumer {
//
//    private static final Logger LOGGER = LogManager.getLogger(KafkaMessageConsumer.class);
//
//    private final ConcurrentLinkedQueue<byte[]> messageQueue = new ConcurrentLinkedQueue<>();
//
//
//    @KafkaListener(topics = "micro_topic",groupId = "my_consumer_group")
//    public void consumeMicro(byte[] encryptedMessage) throws Exception {
//        LOGGER.info("new message consumed at "+ LocalDateTime.now());
//        // Decrypt the encrypted message
//        byte[] decryptedMessage = EncryptionUtil.decrypt(encryptedMessage);
//
//        // Add the decrypted message to the queue for further processing
//        messageQueue.offer(decryptedMessage);
//    }
//
//    public Boolean isQueEmpty(){
//        return messageQueue.isEmpty();
//    }
//
//    // Method to retrieve and process the next message from the queue
//    public byte[] getNextMicroMessage() {
//        return messageQueue.poll(); // Retrieve and remove the next message from the queue
//    }

//}
//
