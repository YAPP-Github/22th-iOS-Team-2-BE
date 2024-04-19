package com.pyonsnalcolor.member;

import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.member.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestValidator {
    private static final String ROLE = "ROLE";
    private static final String GUEST = "GUEST";

    private final JwtTokenProvider jwtTokenProvider;

    public boolean validateIfGuest(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String role = (String) claims.get(ROLE);

        if (role.equals(GUEST)) {
            throw new PyonsnalcolorAuthException(GUEST_FORBIDDEN);
        }
        return true;
    }
}