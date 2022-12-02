package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A abstract class  for all barrel
 *
 * @author Java0
 * @date 2022/11/07
 */
public interface IBarrelTile extends IInventory {

    /**
     * Max inventory size
     */
    public static final int MAX_SIZE = 9;

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
     * Return true if barrel can export recipe result
     *
     * @return boolean
     */
    boolean mayOutput();

    long getTickedGameTime();


    /**
     * Set the time required for the barrel to complete the current recipe
     *
     * @param ticks required time
     */
    public abstract void setCraftCooldown(int ticks);


    /**
     * Return true if barrel is not working
     *
     * @return boolean
     */
    public abstract boolean isIdle();


    /**
     * Return if barrel has talisman
     *
     * @return boolean
     */
    public abstract boolean hasTalisman();

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
     * Return block metadata
     *
     * @return int
     */
    int getBlockMetadata();

    /**
     * Return barrel inventory
     *
     * @return {@link List}<{@link ItemStack}>
     */
    List<ItemStack> getItems();

    /**
     * Drop designated item stack from barrel inventory or drop a new item stack not from barrel inventory
     *
     * @param itemstack    The item stack that need be dropped
     * @param count        Item`s amount
     * @param needDecrSize Whether to drop from bucket
     * @return boolean
     */
    default boolean drop(ItemStack itemstack, int count, boolean needDecrSize) {

        World worldIn = this.getWorld();
        double x = this.getXPos();
        double y = this.getYPos() - 1;
        double z = this.getZPos();

        if (BarrelUtil.bottomIsAir(this)) {
            if(!needDecrSize){
                BehaviorDefaultDispenseItem.doDispense(worldIn, itemstack, 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
            }else {
                BehaviorDefaultDispenseItem.doDispense(worldIn, decrStackSize(itemstack, count), 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
                markDirty();
            }
            return true;
        } else{
            return transferItemsOut(itemstack, count, needDecrSize);
        }
    }

    /**
     * Transfer recipe result or item in barrel to block`s inventory under barrel
     *
     * @param output       recipe result
     * @param count        Item`s amount
     * @param needDecrSize Whether to drop from bucket
     * @return boolean
     */
    default boolean transferItemsOut(ItemStack output, int count, boolean needDecrSize) {
        IInventory iinventory = this.getInventoryForBarrelTransfer();

        if (iinventory == null) {
            return false;
        }

        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata()).getOpposite();

        if (BarrelUtil.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }

        if (!needDecrSize) {
            ItemStack itemStack = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, output, enumfacing);
            if (itemStack.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
        } else {
            ItemStack backups = output.copy();
            ItemStack itemStack = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(output, count), enumfacing);
            if (itemStack.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
            this.setInventorySlotContents(indexOf(output), backups);
        }
        return false;
    }

    /**
     * Remove item from inventory
     *
     * @param index inventory index
     */
    default @NotNull ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getItems(), index);
    }


    /**
     * Get max item stack size in barrel
     *
     * @return int
     */
    @Override
    default int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Get block`s inventory under barrel
     *
     * @return {@link IInventory}
     */
    default IInventory getInventoryForBarrelTransfer() {
        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.getXPos() + (double) enumfacing.getFrontOffsetX(), this.getYPos() + (double) enumfacing.getFrontOffsetY(), this.getZPos() + (double) enumfacing.getFrontOffsetZ());
    }


    /**
     * Split item stack size
     *
     * @param itemStack item stack
     * @param count     amount
     * @return {@link ItemStack}
     */
    default ItemStack decrStackSize(ItemStack itemStack, int count) {
        return ItemStackHelper.getAndSplit(this.getItems(), indexOf(itemStack), count);
    }


    /**
     * Get index of item stack in barrel`s inventory
     *
     * @param itemStack item stack
     * @return int
     */
    default int indexOf(ItemStack itemStack) {
        return this.getItems().indexOf(itemStack);
    }


    /**
     * Return true if barrel`s inventory is empty
     *
     * @return boolean
     */
    default boolean isEmpty() {
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if the count of material in barrel equals stack limit
     *
     * @return boolean
     */
    default boolean isFull() {
        for (ItemStack itemstack : this.getItems()) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }



}
