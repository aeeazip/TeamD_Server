package com.presenty.backend.domain.follow;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "following_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member following;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member follower;

    @Builder
    public Follow(Member following, Member follower) {
        this.following = following;
        this.follower = follower;
    }
}
