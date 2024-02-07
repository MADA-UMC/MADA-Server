package com.umc.mada.custom.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CustomItemsResponse {
    private final List<ItemElementResponse> itemList = new ArrayList<>();

//    public CustomItemsResponse(){
//        this.itemList = new ArrayList<>();
//    }

    public void addItem(ItemElementResponse item){
        itemList.add(item);
    }
//    public CustomItemsResponse of(List<CustomItem> customItems, boolean haveItem){
//        List<ItemsElementResponse> itemsElementResponses = customItems.stream()
//                .map(i -> ItemsElementResponse.of(i, haveItem))
//                .collect(Collectors.toList());
//        return new CustomItemsResponse(itemsElementResponses);
//    }
}
