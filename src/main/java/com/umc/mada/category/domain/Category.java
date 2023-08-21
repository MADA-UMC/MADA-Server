package com.umc.mada.category.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.umc.mada.user.domain.User;
import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "CATEGORY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //카테고리 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId; // 유저 ID

    @Column(name = "category_name", nullable = false)
    private String categoryName; // 카테고리명

    @Column(name = "color", nullable = false, length = 45)
    private String color; // 카테고리 색상

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "icon_id", referencedColumnName = "id", nullable = false)
    private Icon icon; // 아이콘 ID (외래키)

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간

    // 생성자 (필수 필드)
    public Category(User userId, String categoryName, String color, Icon icon) {
        this.userId = userId;
        this.categoryName = categoryName;
        this.color = color;
        this.icon = icon;
    }
}