package com.presenty.backend.domain.paper;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.item.Item;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.wishlist.Wishlist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paper")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paper extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paper_id")
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taker_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member taker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "giver_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member giver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Wishlist wishlist;

    @Builder
    public Paper(String name, String content, Item item, Member taker, Member giver, Wishlist wishlist) {
        this.name = name;
        this.content = content;
        this.item = item;
        this.taker = taker;
        this.giver = giver;
        this.wishlist = wishlist;
    }
}
