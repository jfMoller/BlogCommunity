package me.code.server.service;

import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.GoogleAuthUrlDto;
import me.code.server.dto.response.Result;
import org.springframework.web.bind.annotation.GetMapping;

public interface GoogleLoginService {

    GoogleAuthUrlDto generateAuthUrl(String codeChallenge, String codeChallengeMethod);

    @GetMapping("auth/callback")
    Result<AuthDto> callBackLogin(String code, String codeVerifier);
    
}
