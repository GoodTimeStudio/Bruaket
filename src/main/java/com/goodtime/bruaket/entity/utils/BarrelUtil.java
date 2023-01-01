package com.goodtime.bruaket.entity.utils;

import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class BarrelUtil {

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
    public static boolean bottomIsAir(World worldIn, double xPos, double yPos, double zPos) {
        int x = MathHelper.floor(xPos);
        int y = MathHelper.floor(yPos) - 1;
        int z = MathHelper.floor(zPos);

        Block atDown = worldIn.getBlockState(new BlockPos(x, y, z)).getBlock();

        return atDown.equals(Blocks.AIR);

    }

    //获取最后一个放入的物品
    public static ItemStack getLastPutItem(IBarrelTile barrel) {
        ItemStack itemStack = null;
        for (int i = barrel.getSizeInventory() - 1; i >= 0; i--) {
            itemStack = barrel.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                break;
            }
        }
        return itemStack;
    }

    public static boolean areStacksEqualIgnoreSize (ItemStack stackA, ItemStack stackB) {
        return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

}
