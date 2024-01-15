package me.code.server.dto.response;

import org.springframework.http.HttpStatus;

public class SuccessDto extends Result<SuccessDto> {

    public SuccessDto(HttpStatus status, String message) {
        super(status, message, true);
    }
}
