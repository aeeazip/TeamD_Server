package com.presenty.backend.domain.wishlist;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
}
