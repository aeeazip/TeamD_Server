package com.presenty.backend.domain.wishlist;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wishlist",
        indexes = {
            @Index(name = "IX_wishlist_member_id", columnList = "member_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Wishlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
}
