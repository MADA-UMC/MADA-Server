package com.umc.mada.custom.domain;

import java.util.Arrays;

public enum ItemType {
    I1("color"),
    I2("set"),
    I3("item"),
    I4("background");
    //UNKNOWN("알수없음");

    private final String itemType;

    ItemType(String itemType){
        this.itemType = itemType;
    }

    public String getItemType(){
        return itemType;
    }

//    private static final Map<String, ItemType>

    public static ItemType getTypeCode(String type) throws Exception { //색깔이 들어오면 해당 코드인 I1 반환
        return Arrays.stream(ItemType.values())
                .filter(i -> i.itemType.equals(type))
                .findAny()
                //.orElse(UNKNOWN);
                .orElseThrow(Exception::new); //TODO: exception 커스텀하기
    }
}
