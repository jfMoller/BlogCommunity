package me.code.server.controller;

import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.GoogleAuthUrlDto;
import me.code.server.dto.response.Result;
import me.code.server.service.GoogleLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/google/")
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    @Autowired
    public GoogleLoginController(GoogleLoginService googleLoginService) {
        this.googleLoginService = googleLoginService;
    }

    @GetMapping("auth/url")
    public ResponseEntity<GoogleAuthUrlDto> getAuthUrl() {
        var result = googleLoginService.generateAuthUrl();
        return ResponseEntity.ok(result);
    }

    @GetMapping("auth/callback")
    public ResponseEntity<Result<AuthDto>> callBackLogin(@RequestParam("code") String code) {
        var result = googleLoginService.callBackLogin(code);
        return result.toResponseEntity();
    }


}
