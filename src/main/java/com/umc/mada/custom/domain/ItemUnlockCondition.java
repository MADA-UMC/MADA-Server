package com.umc.mada.custom.domain;


public enum ItemUnlockCondition {
    basic("기본 제공"),
    mission("미션"),
    initialValue("초기 값");

    private final String itemUnlockCondition;

    ItemUnlockCondition(String itemUnlockCondition) {
        this.itemUnlockCondition = itemUnlockCondition;
    }

    public String getItemUnlockCondition(){
        return itemUnlockCondition;
    }
}
