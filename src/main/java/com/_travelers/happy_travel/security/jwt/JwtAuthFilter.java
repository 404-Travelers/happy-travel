package com._travelers.happy_travel.security.jwt;

import com._travelers.happy_travel.users.UserService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;



public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String method = request.getMethod();

        String path = request.getRequestURI();

        if ("GET".equalsIgnoreCase(method)
                && path.startsWith("/destinations")
                && !path.equals("/destinations/auth")) {
            return true;
        }


        Set<String> exactExclusions = Set.of(
                "/login",
                "/api/authenticate",
                "/register",
                "/v1/api/get-token",
                "/swagger-ui.html"
        );


        List<String> prefixExclusions = List.of(
                "/public/",
                "/api/public/",
                "/v3/api-docs/",
                "/swagger-ui/",
                "/swagger-resources/",
                "/webjars/"
        );


        return exactExclusions.contains(path) || prefixExclusions.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtService.isValidToken(token)) {
                throw new AuthenticationCredentialsNotFoundException("Invalid JWT token");
            }

            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: " + ex.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }
}
