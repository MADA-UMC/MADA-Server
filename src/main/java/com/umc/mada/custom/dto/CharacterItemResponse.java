package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterItemResponse {
    private int id;
    private String category;

    public static CharacterItemResponse of(CustomItem customItem){
        return CharacterItemResponse.builder()
                .id(customItem.getId())
                .category(customItem.getCategory())
                .build();
    }
}
