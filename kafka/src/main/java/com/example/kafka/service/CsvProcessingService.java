package com.example.kafka.service;

import com.example.kafka.dtos.CustomerDto;
import com.example.kafka.dtos.CustomerResultWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CsvProcessingService {

    CustomerResultWrapper parseCsvFile(MultipartFile file) throws IOException;

    byte[] generateCustomerCsvFileByteArray(List<CustomerDto> customerDtoList) throws IOException;

    byte[] generateWrongParsedDataCsvFileByteArray(List<String> wrongParsedFileNameList) throws IOException;
}
