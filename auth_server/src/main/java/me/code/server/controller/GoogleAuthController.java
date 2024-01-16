package me.code.server.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import me.code.server.dto.response.TokenDto;
import me.code.server.dto.response.UrlDto;
import me.code.server.security.GoogleOpaqueTokenIntrospector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class GoogleAuthController {

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}")
    private String clientSecret;

    private final static String FRONTEND_CLIENT_URL = "http://localhost:5173";

    private GoogleOpaqueTokenIntrospector introspector;

    public GoogleAuthController(GoogleOpaqueTokenIntrospector introspector) {
        this.introspector = introspector;
    }

    @GetMapping("/api/auth/url")
    public ResponseEntity<UrlDto> auth() {

        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                FRONTEND_CLIENT_URL,
                Arrays.asList("email", "openid"))
                .build();
        return ResponseEntity.ok(new UrlDto(url));

    }

    @GetMapping("api/auth/callback")
    public ResponseEntity<TokenDto> callBack(@RequestParam("code") String code) {
        String token;
        try {
            token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    FRONTEND_CLIENT_URL
            ).execute().getAccessToken();

            var info = introspector.introspect(token);
            System.out.println(info.getAttributes().get("email"));
            System.out.println(info.getAttributes().get("isEmailVerified"));

        } catch (IOException e) {
            throw new RuntimeException("Could not authorize token");
        }
        return ResponseEntity.ok(new TokenDto("Success"));

    }


}
