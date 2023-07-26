package com.umc.mada.category.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id; //카테고리 ID

    @Column(name = "category_name", nullable = false, length = 45)
    private String category_name; // 카테고리명

    @Column(name = "color", nullable = false, length = 45)
    private String color; // 카테고리 색상

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "icon_id", nullable = false)
    private int icon_id; // 아이콘 ID (외래키)

    @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp create_at; // 생성일자

    @Column(name = "update_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp update_at; // 수정일자

    // 생성자 (기본 생성자)
    public Category() {
    }

    // 생성자 (필수 필드)
    public Category(String category_name, String color, int icon_id) {
        this.category_name = category_name;
        this.color = color;
        this.icon_id = icon_id;
    }

    // Getter, Setter 메서드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIcon() {
        return icon_id;
    }

    public void setIcon(int icon_id) {
        this.icon_id = icon_id;
    }

    public Timestamp getCreateAt() {
        return create_at;
    }

    public Timestamp getUpdateAt() {
        return update_at;
    }

}
