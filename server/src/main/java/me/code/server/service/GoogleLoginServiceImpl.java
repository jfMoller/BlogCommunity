package me.code.server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import me.code.server.dto.response.AuthDto;
import me.code.server.dto.response.GoogleAuthUrlDto;
import me.code.server.dto.response.Result;
import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.User;
import me.code.server.repository.UserRepository;
import me.code.server.security.GoogleOpaqueTokenIntrospector;
import me.code.server.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class GoogleLoginServiceImpl implements GoogleLoginService {

    private final static String FRONTEND_ORIGIN_URL = "http://localhost:5173";

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}")
    private String clientSecret;

    private final GoogleOpaqueTokenIntrospector introspector;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public GoogleLoginServiceImpl(
            GoogleOpaqueTokenIntrospector introspector,
            UserRepository userRepository,
            UserDetailsServiceImpl userDetailsService,
            JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.introspector = introspector;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public GoogleAuthUrlDto generateAuthUrl(String codeChallenge, String codeChallengeMethod) {
        GoogleAuthorizationCodeRequestUrl urlRequest = new GoogleAuthorizationCodeRequestUrl(
                clientId, FRONTEND_ORIGIN_URL,
                Arrays.asList("email", "openid"));
        urlRequest.setCodeChallenge(codeChallenge);
        urlRequest.setCodeChallengeMethod("S256");

        var url = urlRequest.build();

        return new GoogleAuthUrlDto(url);
    }

    @Override
    public Result<AuthDto> callBackLogin(String code, String codeVerifier) {
        try {
            String token = exchangeCodeForToken(code, codeVerifier);

            OAuth2AuthenticatedPrincipal principal = introspector.introspect(token);
            String userEmail = (String) principal.getAttributes().get("email");
            boolean isEmailVerified = (boolean) principal.getAttributes().get("isEmailVerified");

            if (tokenHasValidUnregisteredEmail(userEmail, isEmailVerified)) {
                registerNewUser(userEmail);
            }

            return authenticateUser(userEmail);

        } catch (IOException exception) {
            throw new CustomRuntimeException(
                    HttpStatus.UNAUTHORIZED,
                    "Google login failed",
                    exception.getMessage());
        }
    }

    private String exchangeCodeForToken(String code, String codeVerifier) throws IOException {

        var tokenRequest = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                clientId, clientSecret,
                code, FRONTEND_ORIGIN_URL);
        tokenRequest.set("code_verifier", codeVerifier);

        return tokenRequest.execute().getAccessToken();
    }

    private boolean tokenHasValidUnregisteredEmail(String userEmail, boolean isEmailVerified) {
        return userEmail != null && isEmailVerified && (userRepository.isNewUser(userEmail));
    }

    private void registerNewUser(String userEmail) {
        String randomPassword = UUID.randomUUID().toString();
        String encryptedPassword = passwordEncoder.encode(randomPassword);
        User newUser = new User(userEmail, encryptedPassword);

        userRepository.save(newUser);
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
