package com.presenty.backend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.error.dto.ErrorResponse;
import com.presenty.backend.error.exception.ErrorCode;
import com.presenty.backend.security.jwt.JwtTokenProvider;
import com.presenty.backend.security.oauth2.OAuth2UserInfo;
import com.presenty.backend.security.request.OauthLogin;
import com.presenty.backend.security.response.LoginResponse;
import com.presenty.backend.service.MemberService;
import com.presenty.backend.service.dto.MemberTokenDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    private final MemberService memberService;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofMillis(2000L))
            .build();

    @PostMapping("/login/oauth2/token/{registrationId}")
    public ResponseEntity<LoginResponse> loginOauthToken(
            @PathVariable String registrationId, @Validated @RequestBody OauthLogin oauthLogin)
            throws JsonProcessingException {
        String providerId = getProviderId(registrationId, oauthLogin.getAccessToken());
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, providerId);
        String username = userInfo.ofUsername();

        String accessToken = jwtTokenProvider.createAccessToken(username);
        LocalDateTime accessTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(accessToken).toInstant(), ZoneId.systemDefault());
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        LocalDateTime refreshTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(refreshToken).toInstant(), ZoneId.systemDefault());

        Member member =  memberService.login(username, refreshToken, refreshTokenExpiresAt);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .memberId(member.getId())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    private String getProviderId(String registrationId, String accessToken) {
        URI uri = URI.create(getTokenInfoUrl(registrationId));
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
        try {
            String tokenInfoJson = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            return objectMapper.readValue(tokenInfoJson, Map.class).get("id").toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTokenInfoUrl(String registrationId) {
        if ("kakao".equals(registrationId)) {
            return "https://kapi.kakao.com/v1/user/access_token_info";
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider 입니다. provider = " + registrationId);
    }

    @PostMapping("/login/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            return unauthorizedResponse();
        }

        String username = jwtTokenProvider.getSubject(token);
        MemberTokenDto memberToken = memberService.getMemberToken(username);
        if (!token.equals(memberToken.getRefreshToken())) {
            return unauthorizedResponse();
        }

        String accessToken = jwtTokenProvider.createAccessToken(username);
        LocalDateTime accessTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(accessToken).toInstant(), ZoneId.systemDefault());
        String refreshToken = memberToken.getRefreshToken();
        LocalDateTime refreshTokenExpiresAt = memberToken.getRefreshTokenExpiresAt();
        long refreshTokenValidityInDay = jwtTokenProvider.getRefreshTokenValidityInDay();
        long loginDayTerm = Duration.between(
                LocalDateTime.now().plusDays(refreshTokenValidityInDay), refreshTokenExpiresAt).toDays();
        if (loginDayTerm > 0) {
            refreshToken = jwtTokenProvider.createRefreshToken(username);
            refreshTokenExpiresAt = LocalDateTime.ofInstant(
                    jwtTokenProvider.getExpiration(refreshToken).toInstant(), ZoneId.systemDefault());
            memberService.login(username, refreshToken, refreshTokenExpiresAt);
        }

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .memberId(memberToken.getMemberId())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    private static ResponseEntity<ErrorResponse> unauthorizedResponse() {
        ErrorCode errorCode = ErrorCode.HANDLE_UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

}
