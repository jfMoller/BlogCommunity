package me.code.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class AuthDto extends Result<AuthDto> {

    @JsonProperty("userRole")
    private String userRole;

    @JsonProperty("jwtToken")
    private String jwtToken;

    public AuthDto(HttpStatus status, String message, String userRole, String jwtToken) {
        super(status, message, true);
        this.userRole = userRole;
        this.jwtToken = jwtToken;
    }

}
