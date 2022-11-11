package com.goodtime.bruaket.util;

import net.minecraft.item.ItemStack;

public class ItemUtils {
	public static boolean areStacksEqualIgnoreSize (ItemStack stackA, ItemStack stackB) {
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

}
