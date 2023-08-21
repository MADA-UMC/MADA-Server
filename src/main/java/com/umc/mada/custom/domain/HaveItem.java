package com.umc.mada.custom.domain;

import com.umc.mada.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_POSSESSION_ITEM")
public class HaveItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private CustomItem customItem;

    @Column(name = "wearing")
    private boolean wearing;

    @Builder
    public HaveItem(User user, CustomItem customItem){
        this.user = user;
        this.customItem = customItem;
    }

    @Builder
    public HaveItem(User user, CustomItem customItem, boolean wearing) {
        this.user = user;
        this.customItem = customItem;
        this.wearing = wearing;
    }

    public void updateHaveItemWearing(boolean wearing){
        this.wearing = wearing;
    }

}
