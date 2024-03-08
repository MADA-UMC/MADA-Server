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
public class ItemElementResponse {
    private int id;
    private String itemType;
    private boolean have; //유저가 해당 아이템을 소유하고 있는지 여부
    private String category;

    public static ItemElementResponse of(CustomItem customItem, boolean have){
        return ItemElementResponse.builder()
                .id(customItem.getId())
                .itemType(customItem.getItemType().getItemType())
                .have(have)
                .category(customItem.getCategory())
                .build();
    }
}
