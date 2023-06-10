package com.presenty.backend.domain.paper;

import com.presenty.backend.domain.core.BaseEntity;
import com.presenty.backend.domain.item.Item;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.wishlist.Wishlist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paper extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paper_id")
    private Long id;
    @Column(name = "content", length = 255, nullable = false)
    private String content;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member2;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Wishlist wishlist;
}
