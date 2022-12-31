package com.goodtime.bruaket.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class BarrelEnergyStorage extends EnergyStorage {

    public BarrelEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive);
    }

    public BarrelEnergyStorage readFromNBT(NBTTagCompound nbt){
        this.energy = nbt.getInteger("Energy");
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }

        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.energy < 0) {
            this.energy = 0;
        }

        nbt.setInteger("Energy", this.energy);
        return nbt;
    }


    public void modifyEnergyStored(int energy) {

        this.energy += energy;

        if (this.energy > capacity) {
            this.energy = capacity;
        } else if (this.energy < 0) {
            this.energy = 0;
        }
    }

}
