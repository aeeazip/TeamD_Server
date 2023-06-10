package com.presenty.backend.service.dto;

import com.presenty.backend.domain.member.Mbti;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreateRequest {

    @Size(min = 5, max = 45)
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
            message = "비밀번호는 8~20자 영문 대/소문자, 숫자를 사용하세요.")
    private String password;

    @NotNull
    @Past
    private LocalDate birthday;

    @NotNull
    private Mbti mbti;

    private Long imageId;

}
