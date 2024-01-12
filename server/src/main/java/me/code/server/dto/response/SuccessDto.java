package me.code.server.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessDto extends Result {

    public SuccessDto(HttpStatus status, String message) {
        super(status, message, true);
    }

    public ResponseEntity<SuccessDto> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}
