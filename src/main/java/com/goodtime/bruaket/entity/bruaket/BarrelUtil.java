package com.goodtime.bruaket.entity.bruaket;

import com.goodtime.bruaket.items.Talisman;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class BarrelUtil {

    public static boolean pullItems(IBarrelTile barrel) {
        List<EntityItem> items = TileEntityHopper.getCaptureItems(barrel.getWorld(), barrel.getXPos(), barrel.getYPos(), barrel.getZPos());
        if (!items.isEmpty()) {
            EntityItem entityitem = items.get(0);
            return putDropInInventoryAllSlots(null, barrel, entityitem);
        }
        return false;
    }

    /**
     * Put drop in inventory
     *
     * @param source      源
     * @param destination 目地
     * @param entity      实体
     * @return boolean
     */
    public static boolean putDropInInventoryAllSlots(IInventory source, IBarrelTile destination, EntityItem entity) {

        if (entity == null) {
            return false;
        } else {
            ItemStack drop = entity.getItem().copy();

            ItemStack result;

            if (drop.getItem() instanceof Talisman) {
                result = destination.putTalisman((Talisman)drop.getItem());
            } else if(destination.getItems() == null){
                return false;
            } else {
                result = TileEntityHopper.putStackInInventoryAllSlots(source, destination, drop, null);
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


    public static boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint) {
                ItemStack itemStack = isidedinventory.getStackInSlot(k);

                if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxStackSize()) {
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

    //桶下方的方块是否为空气
    public static boolean bottomIsAir(IBarrelTile barrel) {
        World worldIn = barrel.getWorld();
        int x = MathHelper.floor(barrel.getXPos());
        int y = MathHelper.floor(barrel.getYPos()) - 1;
        int z = MathHelper.floor(barrel.getZPos());

        Block atDown = worldIn.getBlockState(new BlockPos(x, y, z)).getBlock();

        return atDown.equals(Blocks.AIR);

    }

    //抛出所有储存的物品(除了符文)
    public static void dropAllItems(IBarrelTile barrel) {
        if(!barrel.isEmpty()){
            for (int i = 0; i < barrel.getSizeInventory(); i++) {
                ItemStack itemStack = barrel.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    barrel.drop(itemStack, itemStack.getCount(), true);
                }
            }
        }else {
            dropTalisman(barrel);
        }
    }

    //获取最后一个放入的物品
    private static ItemStack getLastPutItem(IBarrelTile barrel) {
        ItemStack itemStack = null;
        for (int i = barrel.getSizeInventory() - 1; i >= 0; i--) {
            itemStack = barrel.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                break;
            }
        }
        return itemStack;
    }


    //抛出最后放入的物品
    public static void dropLastItem(IBarrelTile barrel) {
        if(!barrel.isEmpty()){
            ItemStack itemStack = getLastPutItem(barrel);
            if (itemStack != null) {
                barrel.drop(itemStack, itemStack.getCount(), true);
            }
        }else {
            dropTalisman(barrel);
        }
    }

    public static void dropTalisman(IBarrelTile barrel) {
        barrel.drop(new ItemStack(barrel.getTalisman(),1),1, false);
        barrel.setTalisman(null);
    }

}
