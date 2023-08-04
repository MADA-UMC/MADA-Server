package com.umc.mada.custom.domain;

import com.umc.mada.file.domain.File;
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
    private Long id;

    @OneToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "type")
    private String itemType;

    private String unlock_condition;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간

    @Builder
    public CustomItem(File file, String itemType, String unlock_condition){
        this.file = file;
        this.itemType = itemType;
        this.unlock_condition = unlock_condition;
    }


}