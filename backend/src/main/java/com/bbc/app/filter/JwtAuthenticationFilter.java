package com.bbc.app.filter;

import com.bbc.app.service.JwtService;
import com.bbc.app.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from cookies
        String token = extractTokenFromCookies(request);

        // If no token is found, proceed without authentication
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username (or user ID) from the token
        String username = jwtService.extractCustomUserId(token);

        // If username is found and the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtService.isValid(token, userDetails)) {
                // Create the authentication object and set it in the context
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from cookies.
     *
     * @param request the HttpServletRequest object
     * @return the token string if found, otherwise null
     */
    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            // Look for a specific cookie that holds the JWT token, e.g., 'token'
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt-token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null); // Return the token if present, else null
        }
        return null;
    }
}