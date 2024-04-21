package com.example.kafka.service;

import com.example.kafka.dtos.CustomerDto;
import com.example.kafka.dtos.CustomerResultWrapper;
import com.example.kafka.exception.BadRequestException;
import com.example.kafka.model.constants.Constants;
import com.example.kafka.model.constants.ErrorCode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CsvProcessingServiceImpl implements CsvProcessingService {

    private static final Logger LOGGER = LogManager.getLogger(CsvProcessingServiceImpl.class);


    @Override
    public CustomerResultWrapper parseCsvFile(MultipartFile file) throws IOException {
        List<CustomerDto> microCustomerDtoList = new ArrayList<>();
        List<CustomerDto> largeCustomerDtoList = new ArrayList<>();
        String wrongFileName = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
            CSVParser csvParser = new CSVParser(reader, csvFormat);
            for (CSVRecord csvRecord : csvParser) {

                String nationalId = csvRecord.get("NationalId");
                String name = csvRecord.get("Name");
                Double amount = Double.valueOf(csvRecord.get("Amount"));
                CustomerDto customerDto = new CustomerDto(nationalId, name, amount);
                validateCustomerDto(customerDto);
                if (isMicroCustomer(customerDto)) {
                    microCustomerDtoList.add(customerDto);
                } else {
                    largeCustomerDtoList.add(customerDto);
                }

            }

        } catch (IOException | NumberFormatException e) {
            // Handle parsing errors
            wrongFileName = "exp-file" + UUID.randomUUID() + file.getName();

        }
        LOGGER.info("parseCsvFile success");

        return CustomerResultWrapper.builder().largeCustomers(largeCustomerDtoList).microCustomers(microCustomerDtoList).wrongProcessedFileName(wrongFileName).build();
    }

    private boolean isMicroCustomer(CustomerDto customerDto) {
        Double amount = customerDto.getAmount();
        if (amount < 1000) {
            customerDto.setAmount(amount * 0.9);
            return true;
        }
        customerDto.setAmount(amount / 20 * 0.8);
        return false;

    }

    @Override
    public byte[] generateCustomerCsvFileByteArray(List<CustomerDto> customerDtoList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(baos), CSVFormat.DEFAULT)) {
            for (CustomerDto customerDto : customerDtoList) {
                csvPrinter.printRecord(customerDto.getNationalId(), customerDto.getName(), customerDto.getAmount());
            }
            csvPrinter.flush();
        }
        LOGGER.info("generateCustomerCsvFileByteArray file success");

        return baos.toByteArray();
    }

    @Override
    public byte[] generateWrongParsedDataCsvFileByteArray(List<String> wrongParsedFileNameList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(baos), CSVFormat.DEFAULT)) {
            for (String wrongParsedFileName : wrongParsedFileNameList) {
                csvPrinter.printRecord(wrongParsedFileName);
            }
            csvPrinter.flush();
        }
        LOGGER.info("generateWrongParsedDataCsvFileByteArray file success");

        return baos.toByteArray();
    }


    private void validateCustomerDto(CustomerDto customerDto) {

        if (Objects.isNull(customerDto.getNationalId()) || customerDto.getNationalId().isEmpty() || customerDto.getNationalId().isBlank()) {
            throw new BadRequestException(ErrorCode.NID_MUST_NOT_BE_NULL.name(),
                    ErrorCode.NID_MUST_NOT_BE_NULL.getErrorDesc());
        } else if (!customerDto.getNationalId().matches(Constants.EGY_NID_REGEX)) {
            throw new BadRequestException(ErrorCode.NID_NOT_VALID.name(),
                    ErrorCode.NID_NOT_VALID.getErrorDesc());
        }

        if (Objects.isNull(customerDto.getName()) || customerDto.getName().isEmpty() || customerDto.getName().isBlank()) {
            throw new BadRequestException(ErrorCode.NAME_MUST_NOT_BE_NULL.name(),
                    ErrorCode.NAME_MUST_NOT_BE_NULL.getErrorDesc());
        } else if (!customerDto.getName().matches(Constants.LETTERS_ONLY_REGEX)) {
            throw new BadRequestException(ErrorCode.NAME_NOT_VALID.name(),
                    ErrorCode.NAME_NOT_VALID.getErrorDesc());
        }
    }
}
