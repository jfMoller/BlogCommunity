package me.code.server.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.Result;
import me.code.server.dto.response.UrlDto;
import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.User;
import me.code.server.repository.UserRepository;
import me.code.server.security.GoogleOpaqueTokenIntrospector;
import me.code.server.security.JwtProvider;
import me.code.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@RestController
public class GoogleAuthController {

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}")
    private String clientSecret;

    private final static String FRONTEND_ORIGIN_URL = "http://localhost:5173";

    private final GoogleOpaqueTokenIntrospector introspector;

    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public GoogleAuthController(
            GoogleOpaqueTokenIntrospector introspector,
            UserRepository userRepository,
            UserDetailsServiceImpl userDetailsService,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder) {
        this.introspector = introspector;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/api/auth/url")
    public ResponseEntity<UrlDto> getAuthUrl() {
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                FRONTEND_ORIGIN_URL,
                Arrays.asList("email", "openid"))
                .build();
        return ResponseEntity.ok(new UrlDto(url));

    }

    @GetMapping("api/auth/callback")
    public ResponseEntity<Result<AuthDto>> callBackLogin(@RequestParam("code") String code) {
        String userEmail;
        boolean isEmailVerified;

        try {
            String token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    FRONTEND_ORIGIN_URL
            ).execute().getAccessToken();

            OAuth2AuthenticatedPrincipal principal = introspector.introspect(token);
            userEmail = (String) principal.getAttributes().get("email");
            isEmailVerified = (boolean) principal.getAttributes().get("isEmailVerified");

            if (userEmail != null && isEmailVerified) {
                handleNewUser(userEmail);
            }

        } catch (IOException exception) {
            throw new CustomRuntimeException(
                    HttpStatus.UNAUTHORIZED,
                    "Google login failed",
                    exception.getMessage());
        }
        var result = login(userEmail);
        return result.toResponseEntity();

    }

    private void handleNewUser(String userEmail) {
        if (userRepository.isNewUser(userEmail)) {
            String randomPassword = UUID.randomUUID().toString();
            String encryptedPassword = passwordEncoder.encode(randomPassword);
            User newUser = new User(userEmail, encryptedPassword);

            userRepository.save(newUser);
        }
    }

    private AuthDto login(String userEmail) {
        User authUser = userDetailsService.loadUserByUsername(userEmail);
        String jwtToken = jwtProvider.generateToken(authUser);

        return new AuthDto(
                HttpStatus.OK,
                "Login successful",
                authUser.getRole().toString(),
                jwtToken);
    }

}
