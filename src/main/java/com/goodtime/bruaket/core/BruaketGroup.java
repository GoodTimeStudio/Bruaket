package com.goodtime.bruaket.core;

import com.goodtime.bruaket.blocks.SimpleBucket;
import com.goodtime.bruaket.init.Initializer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

public class BruaketGroup extends CreativeTabs {

    public BruaketGroup() {
        super(Bruaket.NAME);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Initializer.logo);
    }
}
