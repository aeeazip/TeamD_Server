package com.presenty.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.presenty.backend.security.jwt.*;
import com.presenty.backend.security.oauth2.OAuth2AuthenticationSuccessHandlerCustom;
import com.presenty.backend.security.oauth2.OAuth2UserServiceCustom;
import com.presenty.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] GET_PERMITTED_URLS = {
            "/code",
    };

    private static final String[] POST_PERMITTED_URLS = {
            "/login/oauth2/token/{registrationId}",
            "/login/reissue"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            OAuth2UserServiceCustom oAuth2UserService,
            OAuth2AuthenticationSuccessHandlerCustom oAuth2AuthenticationSuccessHandler,
            JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtLogoutHandler jwtLogoutHandler,
            JwtLogoutSuccessHandler jwtLogoutSuccessHandler)
            throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .addFilterAfter(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .logout(logout -> logout
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(config -> config
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )

                .authorizeHttpRequests(antz -> antz
                                .requestMatchers(HttpMethod.GET, GET_PERMITTED_URLS).permitAll()
                                .requestMatchers(HttpMethod.POST, POST_PERMITTED_URLS).permitAll()
                                .anyRequest().authenticated()
                        )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
            @Value("${jwt.secret}") String secret) {
        return new JwtTokenProvider(
                accessTokenValidity, refreshTokenValidity, secret);
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(
            JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(
                jwtTokenProvider);
    }

    @Bean
    public JwtLogoutHandler jwtLogoutHandler(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        return new JwtLogoutHandler(jwtTokenProvider, memberService);
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler(
            ObjectMapper objectMapper, MemberService accountService, JwtTokenProvider jwtTokenProvider) {
        return new JwtLogoutSuccessHandler();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }
}
