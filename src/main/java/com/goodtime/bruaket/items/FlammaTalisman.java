package com.goodtime.bruaket.items;

public class FlammaTalisman extends Talisman{

    private final int smeltingSlot;

    private final int maxSmeltingCount;


    public FlammaTalisman(String registerName, int smeltingSlot, int maxSmeltingCount) {
        super(registerName);
        this.smeltingSlot = smeltingSlot;
        this.maxSmeltingCount = maxSmeltingCount;
    }

    public int getSmeltingSlot() {
        return smeltingSlot;
    }

    public int getMaxSmeltingCount() {
        return maxSmeltingCount;
    }
}
