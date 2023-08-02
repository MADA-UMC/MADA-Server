package com.umc.mada.category.dto;

import com.umc.mada.file.domain.File;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
// 카테고리 조회 시 응답 데이터를 전달하는 DTO 클래스
public class CategoryResponseDto {
    private String categoryName; // 카테고리명
    private String color; // 카테고리 색상
    private File iconId; //아이콘 ID
}
