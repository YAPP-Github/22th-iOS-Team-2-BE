package com.pyonsnalcolor.handler;

import com.pyonsnalcolor.exception.PyonsnalcolorAuthException;
import com.pyonsnalcolor.member.enumtype.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.pyonsnalcolor.exception.model.AuthErrorCode.GUEST_FORBIDDEN;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("JwtAccessDeniedHandler authentication : {}", authentication.getAuthorities());

        if (isGuestUser(authentication)) {
            // 게스트 사용자인 경우 커스텀 예외를 던집니다.
            resolver.resolveException(request, response, null, new PyonsnalcolorAuthException(GUEST_FORBIDDEN));
        } else {
            resolver.resolveException(request, response, null, accessDeniedException);
        }
    }

    private boolean isGuestUser(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean hasGuestRole = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.ROLE_GUEST.toString()));
        return hasGuestRole;
    }
}