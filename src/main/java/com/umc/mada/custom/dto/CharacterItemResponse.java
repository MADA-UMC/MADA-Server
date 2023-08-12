package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.*;

@Getter
@NoArgsConstructor
public class CharacterItemResponse {
    private Long id;
    private String itemType;
    private String filePath;

    @Builder
    private CharacterItemResponse(Long id, String itemType, String filePath){
        this.id = id;
        this.itemType = itemType;
        this.filePath = filePath;
    }

    public static CharacterItemResponse of(CustomItem customItem){
        return CharacterItemResponse.builder()
                .id(customItem.getId())
                .itemType(customItem.getItemType().getItemType())
                .filePath(customItem.getFile().getFilePath())
                .build();
    }
}
