package me.code.server.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessDto extends Result implements ResponseEntityConvertible<SuccessDto> {

    public SuccessDto(HttpStatus status, String message) {
        super(status, message, true);
    }

    @Override
    public ResponseEntity<SuccessDto> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}
