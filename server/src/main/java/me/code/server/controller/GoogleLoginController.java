package me.code.server.controller;

import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.GoogleAuthUrlDto;
import me.code.server.dto.response.Result;
import me.code.server.service.GoogleLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/google/")
public class GoogleLoginController {

    private final GoogleLoginServiceImpl googleLoginService;

    @Autowired
    public GoogleLoginController(GoogleLoginServiceImpl googleLoginService) {
        this.googleLoginService = googleLoginService;
    }

    @GetMapping("auth/url")
    public ResponseEntity<GoogleAuthUrlDto> getAuthUrl(
            @RequestParam String code_challenge,
            @RequestParam String code_challenge_method) {
        var result = googleLoginService.generateAuthUrl(code_challenge, code_challenge_method);
        return ResponseEntity.ok(result);
    }

    @GetMapping("auth/callback")
    private ResponseEntity<Result<AuthDto>> callBackLogin(
            @RequestParam String code,
            @RequestParam String code_verifier) {
        var result = googleLoginService.callBackLogin(code, code_verifier);
        return result.toResponseEntity();
    }
}
