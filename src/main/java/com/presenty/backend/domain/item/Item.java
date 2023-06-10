package com.presenty.backend.domain.item;

import com.presenty.backend.domain.core.BaseTimeEntity;
import com.presenty.backend.domain.wishlist.Wishlist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "oneoff", columnDefinition = "INT", nullable = false)
    private int oneoff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Wishlist wishlist;

    @Builder
    public Item(String name, int oneoff, Wishlist wishlist){
        this.name = name;
        this.oneoff = oneoff;
        this.wishlist = wishlist;
    }

    public void update(int oneoff) {
        this.oneoff = oneoff;
    }
}
