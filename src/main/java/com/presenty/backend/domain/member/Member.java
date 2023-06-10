package com.presenty.backend.domain.member;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.image.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "member",
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_member_username", columnNames = "username")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    /**
     * OAuth2 로그인의 경우 password 는 null 로 일반적인 로그인 불가능
     */
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday", nullable = true)
    private LocalDate birthday;

    @Column(name = "mbti", nullable = true)
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @OneToOne(optional = false)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    private Member(
            String username,
            String password) {
        this.username = username;
        this.password = password;
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    public void renewRefreshToken(String refreshToken, LocalDateTime expiresAt) {
        Assert.notNull(Member.this.refreshToken, "refreshToken 은 null 일 수 없습니다.");
        Assert.notNull(expiresAt, "expiresAt 은 null 일 수 없습니다.");
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = expiresAt;
    }

    public void refreshTokenExpires() {
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

}
