package com.goodtime.bruaket.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ItemUtils {
	public static boolean areStacksEqualIgnoreSize (ItemStack stackA, ItemStack stackB) {
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

}
