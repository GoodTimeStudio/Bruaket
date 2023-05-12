package com.goodtime.bruaket.items;

import com.goodtime.bruaket.config.BruaketConfig;

public class FlammaTalisman extends Talisman{

    private final int smeltingSlot;

    private final int maxSmeltingCount;

    //单次耗时（tick）
    private final int smeltTime;

    //每Tick消耗
    private final int smeltWaste;

    //总消耗
    private final int smeltingConsumption;

    public FlammaTalisman(String registerName, int smeltingSlot, int maxSmeltingCount, int smeltTime, int smeltingConsumption) {
        super(registerName);
        this.smeltingSlot = smeltingSlot;
        this.maxSmeltingCount = maxSmeltingCount;
        this.smeltTime = smeltTime;
        this.smeltWaste = smeltingConsumption / smeltTime;
        this.smeltingConsumption = smeltingConsumption * BruaketConfig.smeltingConsumptionMultiplier;
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
