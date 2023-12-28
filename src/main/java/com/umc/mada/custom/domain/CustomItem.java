package com.umc.mada.custom.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CUSTOM_ITEM")
public class CustomItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(nullable = false, length = 40)
    private String category;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ItemType itemType;

    @Column(name = "unlock_condition")
    @Enumerated(value = EnumType.STRING)
    private ItemUnlockCondition unlockCondition;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Builder
    public CustomItem(ItemType itemType, ItemUnlockCondition unlock_condition){
        this.itemType = itemType;
        this.unlockCondition = unlock_condition;
    }


}