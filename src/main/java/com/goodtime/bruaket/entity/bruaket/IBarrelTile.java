package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.items.Talisman;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An interface for all barrel
 *
 * @author Java0
 * @date 2022/11/07
 */
public interface IBarrelTile extends IInventory {

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
     * Return block metadata
     *
     * @return int
     */
    int getBlockMetadata();

    long getTickedGameTime();

    /**
     * Return true if barrel is not working
     *
     * @return boolean
     */
    boolean isIdle();

    /**
     * Set the time required for the barrel to complete the current recipe
     *
     * @param ticks required time
     */
    void setCraftCooldown(int ticks);


    /**
     * Get the remaining time of current work
     *
     * @return int
     */
    int getCraftCooldown();

    /**
     * Return if barrel has talisman
     *
     * @return boolean
     */
    boolean hasTalisman();

    /**
     * Executed when item dropped into the bucket is talisman
     *
     * @param talisman 护身符
     * @return {@link ItemStack}
     */
    ItemStack putTalisman(Talisman talisman);

    /**
     * Set barrel`s talisman
     *
     * @param talisman Barrel`s talisman
     */
    void setTalisman(Talisman talisman);

    /**
     * Get barrel`s talisman
     *
     * @return {@link Talisman}
     */
    Talisman getTalisman();

    /**
     * Return barrel inventory
     *
     * @return {@link List}<{@link ItemStack}>
     */
    List<ItemStack> getItems();


    /**
     * Return true if barrel need match recipe
     *
     * @return boolean
     */
    boolean matchingRequired();

    /**
     * set matching required
     *
     * @return boolean
     */
    void setMatchingRequired(boolean required);


    /**
     * Pull dropped item from top of barrel
     *
     * @return boolean
     */
    boolean pullItems();


    /**
     * Put drop in barrel`s inventory
     *
     * @param source 源
     * @param entity 实体
     * @return boolean
     */
    boolean putDropInInventoryAllSlots(IInventory source, EntityItem entity);


    /**
     * Drop all item from barrel`s inventory
     */
    void dropAllItems();


    /**
     * Drop the last item from the bucket
     */
    void dropLastItem();

    /**
     * Drop the talisman from the bucket
     */
    void dropTalisman();


    /**
     * Return true if barrel can start working
     *
     * @return boolean
     */
    boolean canStart();

    /**
     * Return true if barrel can export recipe result
     *
     * @return boolean
     */
    boolean mayOutput();


    /**
     * Drop designated item stack from barrel inventory or drop a new item stack not from barrel inventory
     *
     * @param itemstack    The item stack that need be dropped
     * @param count        Item`s amount
     * @param needDecrSize Whether to drop from bucket
     * @return boolean
     */
    boolean drop(ItemStack itemstack, int count, boolean needDecrSize);

    /**
     * Transfer recipe result or item in barrel to block`s inventory under barrel
     *
     * @param output       recipe result
     * @param count        Item`s amount
     * @param needDecrSize Whether to drop from bucket
     * @return boolean
     */
    boolean transferItemsOut(ItemStack output, int count, boolean needDecrSize);

    /**
     * Remove item from inventory
     *
     * @param index inventory index
     */
    @NotNull ItemStack removeStackFromSlot(int index) ;


    /**
     * Get max item stack size in barrel
     *
     * @return int
     */
    @Override
    int getInventoryStackLimit();


    /**
     * Get block`s inventory under barrel
     *
     * @return {@link IInventory}
     */
    IInventory getInventoryForBarrelTransfer();


    /**
     * Split item stack size
     *
     * @param itemStack item stack
     * @param count     amount
     * @return {@link ItemStack}
     */
    ItemStack decrStackSize(ItemStack itemStack, int count);


    /**
     * Get index of item stack in barrel`s inventory
     *
     * @param itemStack item stack
     * @return int
     */
    int indexOf(ItemStack itemStack);


    /**
     * Return true if barrel`s inventory is empty
     *
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Return true if the count of material in barrel equals stack limit
     *
     * @return boolean
     */
    boolean isFull();

}
