package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.*;

@Getter
@NoArgsConstructor
public class CharacterItemResponse {
    private int id;
    private String name;
    private String itemType;
    private String filePath;

    @Builder
    private CharacterItemResponse(int id, String name,String itemType, String filePath){
        this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.filePath = filePath;
    }

    public static CharacterItemResponse of(CustomItem customItem){
        return CharacterItemResponse.builder()
                .id(customItem.getId())
                .name(customItem.getName())
                .itemType(customItem.getItemType().getItemType())
                .filePath(customItem.getFilePath())
                .build();
    }
}
