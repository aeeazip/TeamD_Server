package com.presenty.backend.config;

import com.presenty.backend.security.argumentresolver.LoginMemberIdArgumentResolver;
import com.presenty.backend.security.argumentresolver.LoginUsernameArgumentResolver;
import com.presenty.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUsernameArgumentResolver());
        resolvers.add(new LoginMemberIdArgumentResolver(memberService));
    }
}
