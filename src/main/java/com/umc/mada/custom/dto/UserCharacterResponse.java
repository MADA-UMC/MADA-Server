package com.umc.mada.custom.dto;

import com.umc.mada.custom.domain.CustomItem;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserCharacterResponse {
    private final List<CharacterItemResponse> data;

    public UserCharacterResponse(List<CharacterItemResponse> items){
        this.data = items;
    }

    public static UserCharacterResponse of(List<CustomItem> customItems){
        List<CharacterItemResponse> characterItemResponse = customItems.stream()
                .map(CharacterItemResponse::of)
                .collect(Collectors.toList());
        return new UserCharacterResponse(characterItemResponse);
    }

}
