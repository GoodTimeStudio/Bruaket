package com.goodtime.bruaket.entity;

import cofh.core.util.core.EnergyConfig;
import cofh.redstoneflux.impl.EnergyStorage;
import com.goodtime.bruaket.entity.bruaket.BarrelUtil;
import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import com.goodtime.bruaket.items.FlammaTalisman;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileEntityNetherBarrel extends TileEntityLockableLoot implements IBarrelTile, ITickable {

    private FlammaTalisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory;

    private int maxSmeltingCount;

    private long tickedGameTime;

    private ItemStack outputResult;

    public boolean matchingRequired = true;

    private int craftCooldown = -1;

    EnergyConfig energyConfig = new EnergyConfig();

    EnergyStorage energyStorage;


    public TileEntityNetherBarrel() {
    }

    public TileEntityNetherBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
        energyStorage = new EnergyStorage(energyConfig.maxEnergy, energyConfig.maxPower * 4);
    }

    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.craftCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();
            if (this.isIdle()) {
                if(this.mayOutput()){
                    drop(outputResult,1,false);
                    outputResult = null;
                }
                this.setCraftCooldown(0);
            }
            this.updateBarrel();
        }
    }

    protected void updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isFull()) {
                matchingRequired = BarrelUtil.pullItems(this) || matchingRequired;
            }
            if(this.hasTalisman()){
                if (matchingRequired && this.isIdle() && !this.isEmpty()) {
                    IBruaketRecipe recipe = RecipeMatcher.OrdinaryRecipeMatch(barrel, talisman.getRegistryName(), inventory);
                    if(recipe != null){
                        this.outputResult = CraftTweakerMC.getItemStack(recipe.getRecipeOutput());
                        this.setCraftCooldown(recipe.getTime());
                        consumeIngredients(recipe.getIngredients());
                        markDirty();
                    }else{
                        matchingRequired = false;
                    }
                }
            }
        }
    }

    public int calcEnergy() {

        if (energyStorage.getEnergyStored() >= energyConfig.maxPowerLevel) {
            return energyConfig.maxPower;
        }
        if (energyStorage.getEnergyStored() < energyConfig.minPowerLevel) {
            return Math.min(energyConfig.minPower, energyStorage.getEnergyStored());
        }
        return energyStorage.getEnergyStored() / energyConfig.energyRamp;
    }

    @Override
    public boolean mayOutput() {
        return false;
    }

    @Override
    public long getTickedGameTime() {
        return this.tickedGameTime;
    }

    @Override
    public void setCraftCooldown(int ticks) {
        this.craftCooldown = ticks;
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean hasTalisman() {
        return false;
    }

    @Override
    public ItemStack putTalisman(Talisman talisman) {

        if(!(talisman instanceof FlammaTalisman)){
            return new ItemStack(talisman);
        }

        if (!this.hasTalisman()) {
            this.setTalisman(talisman);
            this.markDirty();
            return ItemStack.EMPTY;
        } else {
            return new ItemStack(talisman);
        }
    }

    @Override
    public void setTalisman(Talisman talisman) {
        if(talisman == null){
            this.talisman = null;
        }else {
            FlammaTalisman fTalisman = (FlammaTalisman) talisman;
            this.talisman = fTalisman;
            this.inventory = NonNullList.withSize(fTalisman.getSmeltingSlot(), ItemStack.EMPTY);
            this.maxSmeltingCount = fTalisman.getMaxSmeltingCount();
        }
    }

    @Override
    public Talisman getTalisman() {
        return this.talisman;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void consumeIngredients(RecipeIngredients ingredients) {

    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    public int getBlockMetadata() {
        return super.getBlockMetadata();
    }


    public @NotNull World getWorld(){
        return this.world;
    }

    public double getXPos() {
        return (double)this.pos.getX() + 0.5D;
    }


    public double getYPos() {
        return (double)this.pos.getY() + 0.5D;
    }


    public double getZPos() {
        return (double)this.pos.getZ() + 0.5D;
    }

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
