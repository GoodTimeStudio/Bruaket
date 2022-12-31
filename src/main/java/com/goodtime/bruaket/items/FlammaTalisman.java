package com.goodtime.bruaket.items;

public class FlammaTalisman extends Talisman{

    private final int smeltingSlot;

    private final int maxSmeltingCount;

    private final int smeltTime;

    private final int smeltWaste;

    private final int smeltingConsumption;

    public FlammaTalisman(String registerName, int smeltingSlot, int maxSmeltingCount, int smeltTime, int smeltWaste) {
        super(registerName);
        this.smeltingSlot = smeltingSlot;
        this.maxSmeltingCount = maxSmeltingCount;
        this.smeltTime = smeltTime;
        this.smeltWaste = smeltWaste;
        this.smeltingConsumption = smeltWaste * smeltTime;
    }

    public int getSmeltingSlot() {
        return smeltingSlot;
    }

    public int getMaxSmeltingCount() {
        return maxSmeltingCount;
    }

    public int getSmeltTime() {
        return smeltTime;
    }

    public int getSmeltWaste() {
        return smeltWaste;
    }

    public int getSmeltingConsumption() {
        return smeltingConsumption;
    }
}
