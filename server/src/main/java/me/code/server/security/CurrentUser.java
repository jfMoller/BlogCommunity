package me.code.server.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Annotation to access the currently authenticated user in a Spring Security-protected method.
 * Example usage on a method parameter:
 * {@code
 * public ResponseEntity<?> myMethod(@CurrentUser User currentUser) {
 * // Access the currently authenticated user using 'currentUser'
 * // ...
 * <p>
 * The annotation internally uses {@link AuthenticationPrincipal}
 * with a SpEL expression to load the user by username via the
 * {@code UserDetailsServiceImpl}.
 * <p>
 *
 * @see AuthenticationPrincipal
 * @see me.code.server.security.UserDetailsServiceImpl
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "@userDetailsServiceImpl.loadUserByUsername(getSubject())")
public @interface CurrentUser {
}