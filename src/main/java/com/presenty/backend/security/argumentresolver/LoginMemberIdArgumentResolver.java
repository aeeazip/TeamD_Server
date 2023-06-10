package com.presenty.backend.security.argumentresolver;

import com.presenty.backend.security.annotation.LoginMemberId;
import com.presenty.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentUsernameAnnotation = parameter.hasParameterAnnotation(LoginMemberId.class);
        boolean hasStringType = (Long.class == parameter.getParameterType());
        return hasCurrentUsernameAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<String> usernameOptional = (nonNull(authentication) && authentication.isAuthenticated())
                ? Optional.of(authentication.getName()) : Optional.empty();
        return usernameOptional
                .map(memberService::getMemberId)
                .orElse(null);
    }
}
