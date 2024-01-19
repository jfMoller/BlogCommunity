package me.code.server.service;

import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.GoogleAuthUrlDto;
import me.code.server.dto.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface GoogleLoginService {

    GoogleAuthUrlDto generateAuthUrl();

    @GetMapping("auth/callback")
    Result<AuthDto> callBackLogin(@RequestParam("code") String code);
    
}
