package com.umc.mada.custom.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CustomItemsResponse {
    private final List<ItemsElementResponse> data;

    public CustomItemsResponse(){
        this.data = new ArrayList<>();
    }

    public CustomItemsResponse(List<ItemsElementResponse> itemsList){
        this.data = itemsList;
    }

    public void addItem(ItemsElementResponse item){
        data.add(item);
    }
//    public CustomItemsResponse of(List<CustomItem> customItems, boolean haveItem){
//        List<ItemsElementResponse> itemsElementResponses = customItems.stream()
//                .map(i -> ItemsElementResponse.of(i, haveItem))
//                .collect(Collectors.toList());
//        return new CustomItemsResponse(itemsElementResponses);
//    }
}
