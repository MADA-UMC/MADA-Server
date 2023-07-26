package com.umc.mada.category.domain;

import lombok.*;
import javax.persistence.*;
import com.umc.mada.global.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@Table(name = "CATEGORY")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //카테고리 ID

    @Column(name = "category_name", nullable = false, length = 45)
    private String category_name; // 카테고리명

    @Column(name = "color", nullable = false, length = 45)
    private String color; // 카테고리 색상

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "icon_id", nullable = false)
    @Column(name = "icon_id", nullable = false)
    private int icon_id; // 아이콘 ID (외래키)

    // 생성자 (필수 필드)
    public Category(String category_name, String color, int icon_id) {
        this.category_name = category_name;
        this.color = color;
        this.icon_id = icon_id;
    }

}
