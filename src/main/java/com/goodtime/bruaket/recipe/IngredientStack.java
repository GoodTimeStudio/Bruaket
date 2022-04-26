package com.goodtime.bruaket.recipe;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreIngredient;
import util.ItemUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
    提供一系列合成原料的相关方法
 */

public class IngredientStack {

    private final Ingredient ingredient;

    private int count;

    @Nullable
    private final NBTTagCompound nbt;

    //以ItemStack构建IngredientStack
    public IngredientStack (ItemStack stack) {
        this.ingredient = Ingredient.fromStacks(stack);
        this.count = stack.getCount();
        this.nbt = stack.getTagCompound();
    }

    //以指定数量物品构建IngredientStack
    public IngredientStack (Item item, int count) {
        this(item, count, null);
    }

    //以指定数量Item构建IngredientStack并写入NBT
    public IngredientStack (Item item, int count, NBTTagCompound nbt) {
        this.ingredient = Ingredient.fromItem(item);
        this.count = count;
        this.nbt = nbt;
    }

    //以数量为1的物品构建IngredientStack
    public IngredientStack (Item item) {
        this(item, 1, null);
    }

    //以数量为1的ore构建IngredientStack
    public IngredientStack (String ore) {
        this(ore, 1, null);
    }

    //以指定数量的ore构建IngredientStack
    public IngredientStack (String ore, int count) {
        this(ore, count, null);
    }

    //以指定数量ore构建IngredientStack并写入NBT
    public IngredientStack (String ore, int count, NBTTagCompound nbt) {
        this.ingredient = new OreIngredient(ore);
        this.count = count;
        this.nbt = nbt;
    }

    //以数量为1的Ingredient构建IngredientStack
    public IngredientStack (Ingredient ingredient) {
        this(ingredient, 1, null);
    }

    //以指定数量的Ingredient构建IngredientStack
    public IngredientStack (Ingredient ingredient, int count) {
        this(ingredient, count, null);
    }

    //以指定数量的Ingredient构建IngredientStack并写入NBT
    public IngredientStack (Ingredient ingredient, int count, NBTTagCompound nbt) {
        this.ingredient = ingredient;
        this.count = count;
        this.nbt = nbt;
    }

    //以数量为1的Block构建IngredientStack
    public IngredientStack (Block block) {
        this(block, 1, null);
    }

    //以指定数量的Block构建IngredientStack
    public IngredientStack (Block block, int count) {
        this(block, count, null);
    }

    //以指定数量的Block构建IngredientStack并写入NBT
    public IngredientStack (Block block, int count, NBTTagCompound nbt) {
        this.ingredient = Ingredient.fromItem(Item.getItemFromBlock(block));
        this.count = count;
        this.nbt = nbt;
    }

    public ItemStack[] getMatchingStacks () {
        return ingredient.getMatchingStacks();
    }

    public boolean apply (@Nullable ItemStack p_apply_1_) {
        boolean res = ingredient.apply(p_apply_1_);
        if (nbt != null && p_apply_1_ != null) {
            return res && nbt.equals(p_apply_1_.getTagCompound());
        }

        return res;
    }

    public IntList getValidItemStacksPacked () {
        return ingredient.getValidItemStacksPacked();
    }

    public boolean isSimple () {
        return ingredient.isSimple();
    }

    public int getCount () {
        return count;
    }

    public void shrink (int amount) {
        this.count -= amount;
    }

    public void grow (int amount) {
        this.count += amount;
    }

    public void shrink () {
        shrink(1);
    }

    public void grow () {
        grow(1);
    }

    public Ingredient getIngredient () {
        return ingredient;
    }

    @Nullable
    public NBTTagCompound getNBT () {
        return nbt;
    }

    public List<ItemStack> getMatchingStacksWithSizes () {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : getMatchingStacks()) {
            ItemStack copy = stack.copy();
            copy.setCount(getCount());
            result.add(copy);
        }
        return result;
    }

}
