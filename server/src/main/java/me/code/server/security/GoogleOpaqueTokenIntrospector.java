package me.code.server.security;


import me.code.server.dto.response.UserEmailInfo;
import me.code.server.exception.CustomRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;


    public GoogleOpaqueTokenIntrospector(WebClient userInfoClient) {
        this.userInfoClient = userInfoClient;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        Map<String, Object> attributes = new HashMap<>();

        try {
            UserEmailInfo user = userInfoClient.get()
                    .uri(uriBuilder ->
                            uriBuilder.path("/oauth2/v3/userinfo")
                                    .queryParam("access_token", token).build())
                    .retrieve()
                    .bodyToMono(UserEmailInfo.class)
                    .block();
            attributes.put("email", user.email());
            attributes.put("isEmailVerified", user.email_verified());

        } catch (Exception exception) {
            throw new CustomRuntimeException(
                    HttpStatus.BAD_REQUEST,
                    "Could not introspect Google token",
                    exception.getMessage());
        }

        return new OAuth2AuthenticatedPrincipal() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return attributes;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }
        };
    }
}
