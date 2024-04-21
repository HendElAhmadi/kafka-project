package com.example.kafka.service;

import com.example.kafka.config.KafkaConfig;
//import com.example.kafka.consumer.KafkaMessageConsumer;
import com.example.kafka.dtos.CustomerDto;
import com.example.kafka.dtos.CustomerResultWrapper;
import com.example.kafka.exception.BusinessException;
import com.example.kafka.model.constants.ErrorCode;
import com.example.kafka.utils.EncryptionUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LogManager.getLogger(FileServiceImpl.class);

    @Autowired
    private CsvProcessingService csvProcessingService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroup;

//    @Autowired
//    private KafkaMessageConsumer kafkaMessageConsumer;


    @Override
    public Boolean readAndUploadFiles(MultipartFile[] files) throws IOException, NoSuchAlgorithmException {
        List<CustomerDto> microCustomerDtoList = new ArrayList<>();
        List<CustomerDto> largeCustomerDtoList = new ArrayList<>();
        List<String> wrongProcessedFilesList = new ArrayList<>();

        for (MultipartFile file : files) {
            CustomerResultWrapper customerResultWrapper = csvProcessingService.parseCsvFile(file);
            microCustomerDtoList.addAll(customerResultWrapper.getMicroCustomers());
            largeCustomerDtoList.addAll(customerResultWrapper.getLargeCustomers());
            if (customerResultWrapper.getWrongProcessedFileName() != null)
                wrongProcessedFilesList.add(customerResultWrapper.getWrongProcessedFileName());
        }
        try {
            LOGGER.info("generate csv file");
            if (!microCustomerDtoList.isEmpty()) {
                byte[] microCustomerDtoListFile = csvProcessingService.generateCustomerCsvFileByteArray(microCustomerDtoList);
                sendCsvDataToKafka(microCustomerDtoListFile, "micro_topic");
            }
            if (!largeCustomerDtoList.isEmpty()) {
                byte[] largeCustomerDtoListFile = csvProcessingService.generateCustomerCsvFileByteArray(largeCustomerDtoList);
                sendCsvDataToKafka(largeCustomerDtoListFile, "large_topic");
            }

            if (!wrongProcessedFilesList.isEmpty()) {
                byte[] wrongProcessedFilesListFile = csvProcessingService.generateWrongParsedDataCsvFileByteArray(wrongProcessedFilesList);
                sendCsvDataToKafka(wrongProcessedFilesListFile, "wrong_parsed_topic");
            }

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SOMETHING_WENT_WRONG.getErrorDesc(), ErrorCode.SOMETHING_WENT_WRONG.name());
        }


        return true;
    }

    @Override
    public void sendCsvDataToKafka(byte[] csvData, String kafkaTopic) {

        try (KafkaProducer<byte[], byte[]> producer = new KafkaProducer<>(KafkaConfig.createProducerProperties(bootstrapServers))) {
            SecretKey secretKey = EncryptionUtil.generateAESKey(128);
            byte[] encryptedData = EncryptionUtil.encrypt(csvData, secretKey);

            // Send the encrypted data to Kafka topic
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(kafkaTopic, encryptedData);
            producer.send(record).get();
            LOGGER.info("Message sent successfully to Kafka topic: " + kafkaTopic);
        } catch (Exception e) {
            LOGGER.error("Error sending message to Kafka topic + kafka-topic: " + e.getMessage());
            throw new BusinessException(ErrorCode.SOMETHING_WENT_WRONG.getErrorDesc(), ErrorCode.SOMETHING_WENT_WRONG.name());

        }
    }

    @Override
    public ByteArrayResource readMicroCsvDataFromKafka() throws Exception {
        List<byte[]> allRecords = new ArrayList<>();

        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(KafkaConfig.createConsumerProperties(bootstrapServers, consumerGroup));
//        TopicPartition partition = new TopicPartition("micro_topic", 0); // Replace with your topic and partition number
        consumer.subscribe(Collections.singletonList("micro_topic"));


        ConsumerRecords<byte[], byte[]> records = consumer.poll(1000); // Poll for new messages
        for (ConsumerRecord<byte[], byte[]> record : records) {
            byte[] messageValue = record.value();
            allRecords.add(EncryptionUtil.decrypt(messageValue));
        }


        if (allRecords.isEmpty())
            return null;
        // Create zip file
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
            for (int i = 0; i < allRecords.size(); i++) {
                byte[] csvContent = allRecords.get(i);
                // Add CSV file entry to the zip archive
                ZipEntry zipEntry = new ZipEntry("file" + (i + 1) + ".csv");
                zipOut.putNextEntry(zipEntry);
                zipOut.write(csvContent);
                zipOut.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());

//        if (kafkaMessageConsumer.isQueEmpty())
//            return null;
//        byte[] message=kafkaMessageConsumer.getNextMicroMessage();
//        return new ByteArrayResource(message);
    }
}
