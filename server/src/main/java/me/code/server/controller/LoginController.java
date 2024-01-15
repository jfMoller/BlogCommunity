package me.code.server.controller;

import me.code.server.dto.request.LoginDto;
import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.Result;
import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.User;
import me.code.server.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Result<AuthDto>> login(@RequestBody LoginDto dto) {
        try {
            var authResult = getAuthResult(dto);

            if (authResult.isAuthenticated()) {
                var authUser = (User) authResult.getPrincipal();
                String jwtToken = jwtProvider.generateToken(authUser);

                return new AuthDto(
                        HttpStatus.OK,
                        "Login successful",
                        authUser.getRole().toString(),
                        jwtToken)
                        .toResponseEntity();

            } else {
                throw new CustomRuntimeException(
                        HttpStatus.UNAUTHORIZED,
                        "Authentication failed",
                        "Failed to authenticate user with username: " + dto.username());
            }
        } catch (Exception exception) {
            throw new CustomRuntimeException(
                    HttpStatus.UNAUTHORIZED,
                    "Could not login",
                    exception.getMessage());
        }
    }

    private Authentication getAuthResult(LoginDto dto) {
        var authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(dto.username(), dto.password());

        return authenticationManager.authenticate(authRequest);
    }

}
