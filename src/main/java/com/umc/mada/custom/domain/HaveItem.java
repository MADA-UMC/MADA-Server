package com.umc.mada.custom.domain;

import com.umc.mada.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
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

    @Column(name = "wearing") //TODO: wearingItem 테이블로 분리했기에 바꿔야 함 => 헌데 분리하는게 더 좋을지는 생각해보고 수정다시 해야할 듯
    private boolean wearing;

    public void updateHaveItemWearing(boolean wearing){
        this.wearing = wearing;
    }

}
