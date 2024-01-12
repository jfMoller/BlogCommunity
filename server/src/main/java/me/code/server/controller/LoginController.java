package me.code.server.controller;

import me.code.server.model.User;
import me.code.server.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public record LoginDto(String username, String password) {
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto dto) {

        var authRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(dto.username(), dto.password());
        var authResult =
                authenticationManager.authenticate(authRequest);

        if (authResult.isAuthenticated()) {
            return jwtProvider.generateToken((User) authResult.getPrincipal());
        } else return null;
    }

}
