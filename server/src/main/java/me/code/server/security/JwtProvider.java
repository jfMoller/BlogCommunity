package me.code.server.security;

import me.code.server.exception.CustomRuntimeException;
import me.code.server.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private static final JwsHeader JWS_HEADER = JwsHeader.with(MacAlgorithm.HS256).build();

    public JwtProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(User user) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuer("Self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claimsSet)).getTokenValue();
    }

    public String generateCodeChallengeToken(String codeChallenge, String codeChallengeMethod) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(codeChallenge)
                .claim("method", codeChallengeMethod)
                .issuer("Self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(5, ChronoUnit.SECONDS))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(JWS_HEADER, claimsSet)).getTokenValue();
    }

    public String getCodeChallenge(String codeChallengeToken) {
        try {
            Jwt jwt = jwtDecoder.decode(codeChallengeToken);
            return jwt.getSubject();

        } catch (Exception exception) {
            throw new CustomRuntimeException(
                    HttpStatus.BAD_REQUEST,
                    "Could not decode code challenge token",
                    exception.getMessage());
        }
    }

    public String getCodeChallengeMethod(String codeChallengeToken) {
        try {
            Jwt jwt = jwtDecoder.decode(codeChallengeToken);
            return (String) jwt.getClaims().get("method");

        } catch (Exception exception) {
            throw new CustomRuntimeException(
                    HttpStatus.BAD_REQUEST,
                    "Could not decode code challenge token",
                    exception.getMessage());
        }
    }

}
