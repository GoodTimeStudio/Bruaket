package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.entity.utils.BarrelUtil;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class OrdinaryBarrelTile extends TileEntityLockableLoot implements IBarrelTile, ITickable {

    @Override
    public void markDirty(){
        super.markDirty();
        this.setMatchingRequired(true);
    }

    @Override
    public boolean pullItems() {
        List<EntityItem> items = TileEntityHopper.getCaptureItems(this.getWorld(), this.getXPos(), this.getYPos(), this.getZPos());
        if (!items.isEmpty()) {
            EntityItem entityitem = items.get(0);
            return putDropInInventoryAllSlots(null, entityitem);
        }
        return false;
    }

    public boolean putDropInInventoryAllSlots(IInventory source, EntityItem entity) {

        if (entity == null) {
            return false;
        } else {
            ItemStack drop = entity.getItem().copy();

            ItemStack result;

            if (drop.getItem() instanceof Talisman) {
                result = this.putTalisman((Talisman)drop.getItem());
            }else {
                result = TileEntityHopper.putStackInInventoryAllSlots(source, this, drop, null);
            }

            if (result.isEmpty()) {
                entity.setDead();
                return true;
            } else {
                entity.setItem(result);
                return false;
            }
        }
    }

    public void dropAllItems() {
        if(!this.isEmpty()){
            for (int i = 0; i < this.getSizeInventory(); i++) {
                ItemStack itemStack = this.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    this.drop(itemStack, itemStack.getCount(), true);
                }
            }
        }else {
            dropTalisman();
        }
    }

    public void dropLastItem() {
        if(!this.isEmpty()){
            ItemStack itemStack = BarrelUtil.getLastPutItem(this);
            if (itemStack != null) {
                this.drop(itemStack, itemStack.getCount(), true);
            }
        }else {
            dropTalisman();
        }
    }

    public void dropTalisman() {
        if(this.hasTalisman()){
            this.drop(new ItemStack(this.getTalisman(),1),1, false);
            this.setTalisman(null);
        }
    }


    @Override
    public boolean drop(ItemStack itemstack, int count, boolean needDecrSize) {
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

    @Override
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

    @Override
    public @NotNull ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.getItems(), index);
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public IInventory getInventoryForBarrelTransfer() {
        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.getXPos() + (double) enumfacing.getFrontOffsetX(), this.getYPos() + (double) enumfacing.getFrontOffsetY(), this.getZPos() + (double) enumfacing.getFrontOffsetZ());
    }

    @Override
    public ItemStack decrStackSize(ItemStack itemStack, int count) {
        return ItemStackHelper.getAndSplit(this.getItems(), indexOf(itemStack), count);
    }

    @Override
    public int indexOf(ItemStack itemStack) {
        return this.getItems().indexOf(itemStack);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFull() {
        for (ItemStack itemstack : this.getItems()) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull World getWorld(){
        return this.world;
    }

    @Override
    public double getXPos() {
        return (double)this.pos.getX() + 0.5D;
    }

    @Override

    public double getYPos() {
        return (double)this.pos.getY() + 0.5D;
    }

    @Override
    public double getZPos() {
        return (double)this.pos.getZ() + 0.5D;
    }


    @Override
    public Container createContainer(@NotNull InventoryPlayer playerInventory, @NotNull EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

}
