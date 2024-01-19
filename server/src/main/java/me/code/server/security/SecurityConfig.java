package me.code.server.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import me.code.server.repository.UserRepository;
import me.code.server.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/login"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        (auth ->
                                auth
                                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                        .requestMatchers(
                                                "/api/login",
                                                "api/google/auth/url",
                                                "api/google/auth/callback",
                                                "/api/blogs/all",
                                                "/api/blogs/{blogId}",
                                                "/api/blogs/search"
                                        ).permitAll()
                                        .anyRequest().authenticated()))
                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwtConfigurer ->
                                                jwtConfigurer
                                                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        var dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(dao);
    }

    private SecretKey getSecretKey() {
        String secretKey = getJwtSecretKey();
        return new SecretKeySpec(
                secretKey.getBytes(),
                "HmacSHA256");
    }

    private String getJwtSecretKey() {
        InputStream inputStream = SecurityConfig.class.getClassLoader().getResourceAsStream("secrets-config.yml");
        Map<String, String> config = new Yaml().load(inputStream);

        return config.get("JWT_SECRET_KEY");
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWKSource<SecurityContext> secret = new ImmutableSecret<>(getSecretKey());
        return new NimbusJwtEncoder(secret);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey()).build();
    }
}
