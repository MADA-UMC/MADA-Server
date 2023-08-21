package com.umc.mada.custom.domain;


public enum ItemUnlockCondition {
    C0("기본 제공"),
    C1("미션"),
    C2("프리미엄 구독");

    private final String itemUnlockCondition;

    ItemUnlockCondition(String itemUnlockCondition) {
        this.itemUnlockCondition = itemUnlockCondition;
    }

    public String getItemUnlockCondition(){
        return itemUnlockCondition;
    }
}
