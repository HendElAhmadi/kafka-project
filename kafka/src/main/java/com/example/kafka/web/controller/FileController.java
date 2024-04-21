package com.example.kafka.web.controller;

import com.example.kafka.service.FileService;
import com.example.kafka.web.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/upload")
    ResponseEntity<ResponseModel<Boolean>> readAndUploadFile(@RequestParam("files") MultipartFile[] files) throws IOException, NoSuchAlgorithmException {
        Boolean isFileUploaded = fileService.readAndUploadFiles(files);
        ResponseModel<Boolean> responseModel = ResponseModel.<Boolean>builder()
                .status(HttpStatus.OK.value()).data(isFileUploaded).build();
        return ResponseEntity.status(responseModel.getStatus()).body(responseModel);
    }

    @GetMapping("/downloadMicroCSV")
    public ResponseEntity<ByteArrayResource> downloadMicroCSV() throws Exception {
        ByteArrayResource returnedFile = fileService.readMicroCsvDataFromKafka();
        if (returnedFile != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=decrypted_data.csv");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(returnedFile);
        }
        //when no decrypted data is available
        return ResponseEntity.notFound().build();

    }


}
