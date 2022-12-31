package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.energy.BarrelEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class PoweredBarrel extends OrdinaryBarrelTile {

    protected BarrelEnergyStorage energyStorage = new BarrelEnergyStorage(0,0);

    public PoweredBarrel() {
    }

    public boolean smallStorage() {
        return false;
    }

    protected boolean hasEnergy(int energy) {
        return this.energyStorage.getEnergyStored() >= energy;
    }

    protected int getEnergySpace() {
        return this.energyStorage.getMaxEnergyStored() - this.energyStorage.getEnergyStored();
    }


    public BarrelEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energyStorage.readFromNBT(nbt);
    }

    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.energyStorage.writeToNBT(nbt);
        return nbt;
    }

   /* public int getInfoEnergyPerTick() {
        return 0;
    }

    public int getInfoMaxEnergyPerTick() {
        return 0;
    }

    public int getInfoEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }*/

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return this.energyStorage.receiveEnergy(maxReceive, simulate);
    }

    public int getEnergyStored(EnumFacing from) {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return this.energyStorage.getMaxEnergyStored();
    }

    public boolean canConnectEnergy(EnumFacing from) {
        return this.energyStorage.getMaxEnergyStored() > 0;
    }

    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing from) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, from);
    }

    public <T> T getCapability(@NotNull Capability<T> capability, final EnumFacing from) {
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(new IEnergyStorage() {
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return PoweredBarrel.this.receiveEnergy(from, maxReceive, simulate);
            }

            public int extractEnergy(int maxExtract, boolean simulate) {
                return 0;
            }

            public int getEnergyStored() {
                return PoweredBarrel.this.getEnergyStored(from);
            }

            public int getMaxEnergyStored() {
                return PoweredBarrel.this.getMaxEnergyStored(from);
            }

            public boolean canExtract() {
                return false;
            }

            public boolean canReceive() {
                return true;
            }
        }) : super.getCapability(capability, from);
    }

   /* protected int calcEnergy(){

        if(energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored()){
            return  energyStorage.getMaxEnergyStored();
        }

        if(energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored() / 10){
            return Math.min(energyStorage.getMaxEnergyStored() / 10, energyStorage.getEnergyStored());
        }

        return energyStorage.getEnergyStored() / 900;
    }
*/
    @Override
    public String getName() {
        return null;
    }


    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return null;
    }

}
