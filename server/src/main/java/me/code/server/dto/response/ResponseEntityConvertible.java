package me.code.server.dto.response;

import org.springframework.http.ResponseEntity;

public interface ResponseEntityConvertible<T extends Result> {

    ResponseEntity<T> toResponseEntity();
}
