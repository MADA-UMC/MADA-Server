package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemElementResponseDto {
    private int id;
    private String itemType;
    private String category;

    public static ItemElementResponseDto of(CustomItem customItem){
        return ItemElementResponseDto.builder()
                .id(customItem.getId())
                .itemType(customItem.getItemType().getItemType())
                .category(customItem.getCategory())
                .build();
    }
}
