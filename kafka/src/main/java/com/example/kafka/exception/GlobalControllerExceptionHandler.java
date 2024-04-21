package com.example.kafka.exception;

import com.example.kafka.model.constants.ErrorCode;
import com.example.kafka.web.response.ResponseModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(GlobalControllerExceptionHandler.class);


    @ExceptionHandler(value = BadRequestException.class)
    public @ResponseBody ResponseEntity<ResponseModel<Object>> handleBadRequestExceptionException(BadRequestException ex) {
        LOGGER.info(ex.getErrorDesc());
        ResponseModel<Object> error = ResponseModel.builder().status(HttpStatus.BAD_REQUEST.value())
                .errorCode(ex.getErrorCode()).errorDesc(ex.getErrorDesc()).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }


    @ExceptionHandler(value = BusinessException.class)
    public @ResponseBody ResponseEntity<ResponseModel<Object>> handleBusinessException(BusinessException ex) {
        LOGGER.info(ex.getErrorDesc());
        ResponseModel<Object> error = ResponseModel.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ex.getErrorCode()).errorDesc(ex.getErrorDesc()).build();
        return ResponseEntity.status(error.getStatus()).body(error);

    }


    @ExceptionHandler(value = AuthException.class)
    public @ResponseBody ResponseEntity<ResponseModel<Object>> handleAuthException(AuthException ex) {
        LOGGER.info(ex.getErrorDesc());
        ResponseModel<Object> error = ResponseModel.builder().status(HttpStatus.UNAUTHORIZED.value())
                .errorCode(ex.getErrorCode()).errorDesc(ex.getErrorDesc()).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody ResponseEntity<ResponseModel<Object>> handleAuthenticationException(AuthException ex) {
        LOGGER.info(ex.getMessage());
        ResponseModel<Object> error = ResponseModel.builder().status(HttpStatus.FORBIDDEN.value())
                .errorCode(ErrorCode.ACCESS_IS_NOT_ALLOWED.name()).errorDesc(ErrorCode.ACCESS_IS_NOT_ALLOWED.getErrorDesc()).build();
        return ResponseEntity.status(error.getStatus()).body(error);
    }


    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ResponseEntity<ResponseModel<Object>> handleException(Exception ex) {
        LOGGER.error(ex.getMessage());
        ResponseModel<Object> error = ResponseModel.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCode.SOMETHING_WENT_WRONG.name())
                .errorDesc(ErrorCode.SOMETHING_WENT_WRONG.getErrorDesc()).build();
        return ResponseEntity.status(error.getStatus()).body(error);

    }

}
