package com.goodtime.bruaket.entity.barrel;

import com.goodtime.bruaket.items.Talisman;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

/**
 * A interface for all barrel
 *
 * @author ETO
 * @date 2022/11/07
 */
public abstract class BarrelTileEntity extends TileEntityLockableLoot implements IInventory, ITickable {

    /**
     * Max inventory size
     */
    public static final int MAX_SIZE = 9;

    public BarrelTileEntity() {
    }

    /**
     * Get the world of the barrel
     *
     * @return {@link World}
     */
    public World getWorld(){
        return this.world;
    }

    /**
     * Get barrel x pos
     *
     * @return double
     */
    public abstract double getXPos();

    /**
     * Get barrel y pos
     *
     * @return double
     */
    public abstract double getYPos();

    /**
     * Get barrel z pos
     *
     * @return double
     */
    public abstract double getZPos();

    /**
     * Return true if the count of material in barrel equals stack limit
     *
     * @return boolean
     */
    public abstract boolean isFull();


    /**
     * Return true if barrel can export recipe result
     *
     * @return boolean
     */
    public abstract boolean mayTransfer();

    public abstract long getTickedGameTime();


    public abstract void setTransferCooldown(int ticks);

    public abstract boolean isOnTransferCooldown();

    public abstract boolean drop(ItemStack itemstack, int count, boolean needDecrSize);

    public abstract boolean hasTalisMan();

    public abstract void setTalisMan(Talisman talisman);

    public abstract Talisman getTalisMan();


}
