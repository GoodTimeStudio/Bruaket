package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.blocks.Barrel;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.UUID;

public class TileEntityBarrel extends TileEntityHopper {

    private UUID owner = null;

    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(10, ItemStack.EMPTY);

    //传输速度
    private int transferCooldown = -1;
    private long tickedGameTime;

    //读取NBT，确保物品在重启游戏后不会丢失且数量不变
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");
    }

    //存储NBT
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    public void update()
    {
        if (this.world != null && !this.world.isRemote)
        {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();

            if (!this.isOnTransferCooldown())
            {
                this.setTransferCooldown(0);
                this.updateBarrel();
            }
        }
    }

    //漏斗执行的需要时间的操作
    protected boolean updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isOnTransferCooldown() && Barrel.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;

                //如果库存不为空，输出物品
                if (!this.isInventoryEmpty()) {
                    flag = this.transferItemsOut();
                }

                //如果漏斗没满，接收投掷物
                if (!this.isFull()) {
                    flag = pullItems(this) || flag;
                }

                //8tick传输一个物品
                if (flag) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        }
        else {
            return false;
        }
    }

    public static boolean pullItems(IHopper hopper) {
       /* Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(hopper);
        if (ret != null) return ret;*/

        List<EntityItem> items = getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos());
        if(!items.isEmpty()){
            EntityItem entityitem = items.get(0);
            return putDropInInventoryAllSlots((IInventory)null, hopper, entityitem);
        }
        return false;
    }

    //传输物品，如果底部方块没有储存功能就传输失败
    private boolean transferItemsOut() {
        if (net.minecraftforge.items.VanillaInventoryCodeHooks.insertHook(this)) { return true; }
        IInventory iinventory = this.getInventoryForBarrelTransfer();

        //如果检测不到有存储功能的方块，就把合成产出丢出来
        if (iinventory == null) {

            for (int i = 0; i < this.getSizeInventory() - 1; i++) {
                this.setInventorySlotContents(i, ItemStack.EMPTY);
            }

            dispense(this,new ItemStack(Items.APPLE));
            return true;
        }
        else {
            EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata()).getOpposite();

            if (this.isInventoryFull(iinventory, enumfacing)) {
                return false;
            }
            else {
                for (int i = 0; i < this.getSizeInventory()-1; ++i) {
                    if (!this.getStackInSlot(i).isEmpty()) {
                        ItemStack itemstack = this.getStackInSlot(i).copy();
                        ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1), enumfacing);

                        if (itemstack1.isEmpty()) {
                            iinventory.markDirty();
                            return true;
                        }
                        this.setInventorySlotContents(i, itemstack);
                    }
                }
                return false;
            }
        }
    }

    //冒尖冒尖！
    private void dispense(IHopper barrel, ItemStack itemstack){
        double x = barrel.getXPos();
        double y = barrel.getYPos() - 1;
        double z = barrel.getZPos();
        BehaviorDefaultDispenseItem.doDispense(barrel.getWorld(), itemstack, 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
    }

    //获取桶底部的方块的库存（inventory），如果没有库存就返回null
    private IInventory getInventoryForBarrelTransfer() {
        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata());
        return getInventoryAtPosition(this.getWorld(), this.getXPos() + (double)enumfacing.getFrontOffsetX(), this.getYPos() + (double)enumfacing.getFrontOffsetY(), this.getZPos() + (double)enumfacing.getFrontOffsetZ());
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        } else {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isInventoryEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    //全部格子是否都满了
    private boolean isFull() {
        //遍历所有格子，如果格子是空的，或者物品的最大量不为可储存的最大量
        for (ItemStack itemstack : this.inventory) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.inventory;
    }


    public int getSizeInventory() {
        return this.inventory.size();
    }


    public void setOwner(UUID owner) {
        this.owner = owner;
    }

}
