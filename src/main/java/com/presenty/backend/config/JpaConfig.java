package com.presenty.backend.config;

import com.presenty.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaConfig {

    private final MemberService memberService;


    @Bean
    public AuditorAware<Long> auditorAware() {

        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Long> memberIdOptional = (nonNull(authentication) && authentication.isAuthenticated())
                    ? Optional.of(memberService.getMemberId(authentication.getName())) : Optional.empty();
            return memberIdOptional;
        };
    }
}
