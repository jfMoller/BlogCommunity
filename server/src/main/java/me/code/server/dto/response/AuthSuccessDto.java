package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class AuthSuccessDto extends SuccessDto {

    @JsonProperty("userRole")
    private String userRole;

    @JsonProperty("jwtToken")
    private String jwtToken;

    public AuthSuccessDto(HttpStatus status, String message, String userRole, String jwtToken) {
        super(status, message);
        this.userRole = userRole;
        this.jwtToken = jwtToken;
    }

}
