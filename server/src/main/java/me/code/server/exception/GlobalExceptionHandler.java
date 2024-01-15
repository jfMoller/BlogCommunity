package me.code.server.exception;

import me.code.server.dto.response.ErrorDto;
import me.code.server.dto.response.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Result<ErrorDto>> buildResponseEntity(CustomRuntimeException exception) {
        HttpStatus status = exception.getStatus();
        String message = exception.getMessage();
        String errorDetails = exception.getExceptionMessage();

        return new ErrorDto(status, message, errorDetails).toResponseEntity();
    }

    @ExceptionHandler({CustomRuntimeException.class})
    public ResponseEntity<Result<ErrorDto>> handleUncheckedException(CustomRuntimeException exception) {
        return buildResponseEntity(exception);
    }

}