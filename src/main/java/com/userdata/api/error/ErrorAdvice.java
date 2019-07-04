package com.userdata.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAdvice.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity handleBusinessException(BusinessException exception) {
        LOGGER.error(" BusinessException encountered" + exception.getMessage());
        return createErrorResponse(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity handleAllException(RuntimeException exception) {
        LOGGER.error(" Runtime exception encountered " + exception.getMessage());
        return createErrorResponse(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while processing request"));
    }

    private ResponseEntity createErrorResponse(ApiErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getHttpStatusCode()));
    }

}
