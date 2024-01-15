package me.code.server.controller;

import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.Result;
import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.User;
import me.code.server.repository.UserRepository;
import me.code.server.security.JwtProvider;
import me.code.server.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("google")
public class GoogleLoginController {

    private static final String USER_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    private record UserEmailResponse(String email) {
    }

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public GoogleLoginController(
            UserRepository userRepository,
            UserDetailsServiceImpl userDetailsService,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public ResponseEntity<Result<AuthDto>> login(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient client) {
        String token = client.getAccessToken().getTokenValue();

        HttpHeaders headers = createHeadersWithBearerAuth(token);

        RequestEntity<Void> request = createRequestEntity(headers);

        ResponseEntity<UserEmailResponse> response = requestUserEmail(request);

        if (isSuccessfulResponse(response)) {
            String userEmail = response.getBody().email();

            handleNewUser(userEmail);

            AuthDto authDto = authenticateUser(userEmail);
            return authDto.toResponseEntity();
        }

        throw new CustomRuntimeException(
                HttpStatus.UNAUTHORIZED,
                "Authorization failed",
                "Could not retrieve google user email");
    }

    private HttpHeaders createHeadersWithBearerAuth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private RequestEntity<Void> createRequestEntity(HttpHeaders headers) {
        return RequestEntity.get(URI.create(USER_INFO_ENDPOINT)).headers(headers).build();
    }

    private ResponseEntity<UserEmailResponse> requestUserEmail(RequestEntity<Void> request) {
        return new RestTemplate().exchange(request, UserEmailResponse.class);
    }

    private boolean isSuccessfulResponse(ResponseEntity<UserEmailResponse> response) {
        return response.getStatusCode().is2xxSuccessful();
    }

    private void handleNewUser(String userEmail) {
        if (userRepository.isNewUser(userEmail)) {
            String randomPassword = UUID.randomUUID().toString();
            String encryptedPassword = passwordEncoder.encode(randomPassword);
            User newUser = new User(userEmail, encryptedPassword);

            userRepository.save(newUser);
        }
    }

    private AuthDto authenticateUser(String userEmail) {
        User authUser = userDetailsService.loadUserByUsername(userEmail);
        String jwtToken = jwtProvider.generateToken(authUser);

        return new AuthDto(
                HttpStatus.OK,
                "Login successful",
                authUser.getRole().toString(),
                jwtToken);
    }

}
