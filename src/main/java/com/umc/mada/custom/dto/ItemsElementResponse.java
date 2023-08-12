package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.*;

@Getter
public class ItemsElementResponse {
    private Long id;
    private String itemType;
    private String itemUnlockCondition; //TODO: 미션이라면 미션 내용도 반환하기 위해 mission변수 추가해야함
    private String filePath;
    private boolean have; //유저가 해당 아이템을 소유하고 있는지 여부

    @Builder
    private ItemsElementResponse(Long id, String itemType, String itemUnlockCondition,String filePath, boolean have){
        this.id = id;
        this.itemType = itemType;
        this.itemUnlockCondition = itemUnlockCondition;
        this.filePath = filePath;
        this.have = have;
    }

    public static ItemsElementResponse of(CustomItem customItem, boolean have){
        return ItemsElementResponse.builder()
                .id(customItem.getId())
                .itemType(customItem.getItemType().getItemType())
                .itemUnlockCondition(customItem.getUnlock_condition())
                .filePath(customItem.getFile().getFilePath())
                .have(have)
                .build();
    }
}
