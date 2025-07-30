package com._travelers.happy_travel.security.jwt;

import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.UserRepository;
import com._travelers.happy_travel.users.dto.UserLoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String JWT_SECRET_KEY = "mySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLong";
    private final Long JWT_EXPIRATION = 1800000L;
    private final String ROLE = "role";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public String generateToken(CustomUserDetail userDetail) {
            return buildToken(userDetail, JWT_EXPIRATION);
        }

    private String buildToken(CustomUserDetail userDetail, long jwtExpiration) {
        return Jwts
                .builder()
                .claim(ROLE, userDetail.getAuthorities().toString())
                .subject(userDetail.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername (String token) {
            return extractAllClaims(token).getSubject();
        }

    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public JwtResponse loginAuthentication (UserLoginRequest userLoginRequest){
        userRepository.findByUsername(userLoginRequest.username()).orElseThrow(() -> new EntityNotFoundException("User", "username", userLoginRequest.username()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.username(), userLoginRequest.password()));
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String token = this.generateToken(userDetail);
        return new JwtResponse(token);
    }
}
