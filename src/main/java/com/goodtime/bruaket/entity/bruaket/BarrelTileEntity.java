package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * A abstract class  for all barrel
 *
 * @author Java0
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

    public double getXPos() {
        return (double)this.pos.getX() + 0.5D;
    }


    /**
     * Get barrel y pos
     *
     * @return double
     */
    public double getYPos() {
        return (double)this.pos.getY() + 0.5D;
    }

    /**
     * Get barrel z pos
     *
     * @return double
     */
    public double getZPos() {
        return (double)this.pos.getZ() + 0.5D;
    }

    /**
     * Return true if barrel can export recipe result
     *
     * @return boolean
     */
    public abstract boolean mayOutput();

    public abstract long getTickedGameTime();


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
    public abstract void setTalisman(Talisman talisman);

    /**
     * Get barrel`s talisman
     *
     * @return {@link Talisman}
     */
    public abstract Talisman getTalisman();


    /**
     * Drop designated item stack from barrel inventory or drop a new item stack not from barrel inventory
     *
     * @param itemstack    The item stack that need be dropped
     * @param count        Item`s amount
     * @param needDecrSize Whether to drop from bucket
     * @return boolean
     */
    public boolean drop(ItemStack itemstack, int count, boolean needDecrSize) {

        World worldIn = this.world;
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
    public boolean transferItemsOut(ItemStack output, int count, boolean needDecrSize) {
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
     * Set inventory contents
     *
     * @param index inventory index
     * @param stack item stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.fillWithLoot(null);
        this.getItems().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    /**
     * Remove item from inventory
     *
     * @param index inventory index
     */
    public @NotNull ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getItems(), index);
    }


    /**
     * Get max item stack size in barrel
     *
     * @return int
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Get block`s inventory under barrel
     *
     * @return {@link IInventory}
     */
    private IInventory getInventoryForBarrelTransfer() {
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
    private ItemStack decrStackSize(ItemStack itemStack, int count) {
        return ItemStackHelper.getAndSplit(this.getItems(), indexOf(itemStack), count);
    }


    /**
     * Get index of item stack in barrel`s inventory
     *
     * @param itemStack item stack
     * @return int
     */
    private int indexOf(ItemStack itemStack) {
        return this.getItems().indexOf(itemStack);
    }


    /**
     * Return true if barrel`s inventory is empty
     *
     * @return boolean
     */
    public boolean isEmpty() {
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
    public boolean isFull() {
        for (ItemStack itemstack : this.getItems()) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }



}
