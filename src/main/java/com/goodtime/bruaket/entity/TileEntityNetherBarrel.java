package com.goodtime.bruaket.entity;

import cofh.core.block.TilePowered;
import cofh.core.util.core.EnergyConfig;
import cofh.redstoneflux.impl.EnergyStorage;
import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileEntityNetherBarrel extends TilePowered implements IBarrelTile, ITickable {

    private Talisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory;

    private long tickedGameTime;

    private ItemStack outputResult;

    public boolean matchingRequired = true;

    private int craftCooldown = -1;

    EnergyConfig energyConfig = new EnergyConfig();


    public TileEntityNetherBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
        energyStorage = new EnergyStorage(energyConfig.maxEnergy, energyConfig.maxPower * 4);
    }

    @Override
    public void update() {

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
        return 0;
    }

    @Override
    public void setCraftCooldown(int ticks) {

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
    public void setTalisman(Talisman talisman) {

    }

    @Override
    public Talisman getTalisman() {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return 0;
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
    public int getNumPasses() {
        return 0;
    }

    @Override
    public TextureAtlasSprite getTexture(int i, int i1) {
        return null;
    }

    @Override
    protected Object getMod() {
        return null;
    }

    @Override
    protected String getModVersion() {
        return null;
    }

    @Override
    protected String getTileName() {
        return null;
    }
}
