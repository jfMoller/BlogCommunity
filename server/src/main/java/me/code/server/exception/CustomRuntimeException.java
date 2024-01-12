package me.code.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final HttpStatus status;
    private final String exceptionMessage;

    public CustomRuntimeException(HttpStatus status, String customMessage, String exceptionMessage) {
        super(customMessage);
        this.status = status;
        this.exceptionMessage = exceptionMessage;
    }
}
