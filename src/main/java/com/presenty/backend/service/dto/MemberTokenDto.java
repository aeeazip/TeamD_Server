package com.presenty.backend.service.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class MemberTokenDto {

    Long memberId;

    String refreshToken;

    LocalDateTime refreshTokenExpiresAt;
}
