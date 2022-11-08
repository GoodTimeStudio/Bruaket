package com.goodtime.bruaket.entity.barrel;

import com.goodtime.bruaket.items.Talisman;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * A interface for all barrel
 *
 * @author ETO
 * @date 2022/11/07
 */
public interface IBarrel extends IInventory {

    /**
     * Max inventory size
     */
    int MAX_SIZE = 9;

    /**
     * Get the world of the barrel
     *
     * @return {@link World}
     */
    World getWorld();


    /**
     * Get barrel x pos
     *
     * @return double
     */
    double getXPos();

    /**
     * Get barrel y pos
     *
     * @return double
     */
    double getYPos();

    /**
     * Get barrel z pos
     *
     * @return double
     */
    double getZPos();

    /**
     * Return true if the count of material in barrel equals stack limit
     *
     * @return boolean
     */
    boolean isFull();


    /**
     * Return true if barrel can export recipe result
     *
     * @return boolean
     */
    boolean mayTransfer();

    long getTickedGameTime();


    void setTransferCooldown(int ticks);

    boolean isOnTransferCooldown();

    boolean drop(ItemStack itemstack, int count, boolean needDecrSize);

    boolean hasTalisMan();

    void setTalisMan(Talisman talisman);

    Talisman getTalisMan();


}
