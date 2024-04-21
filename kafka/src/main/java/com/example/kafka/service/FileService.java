package com.example.kafka.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileService {

        Boolean readAndUploadFiles(MultipartFile[] file) throws IOException, NoSuchAlgorithmException;

        void sendCsvDataToKafka(byte[] csvData, String kafkaTopic);

        ByteArrayResource readMicroCsvDataFromKafka() throws Exception;
}
