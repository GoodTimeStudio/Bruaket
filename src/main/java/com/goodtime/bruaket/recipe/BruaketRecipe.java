package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BruaketRecipe implements IBruaketRecipe {

    /**
     * 合成所用的桶
     */
    private final ResourceLocation barrel;

    private final RecipeIngredients ingredients;

    /**
     * 合成产出
     */
    private final IIngredient result;

    /**
     * 合成所需时间
     */
    private final int time;

    public BruaketRecipe(@Nonnull ResourceLocation barrel, @Nonnull IIngredient result, int time, @Nonnull RecipeIngredients ingredients) {
        this.barrel = barrel;
        this.ingredients = ingredients;
        this.result = result;
        this.time = time;
    }

    @Override
    public ResourceLocation getBarrel() {
        return this.barrel;
    }

    @Override
    public long getTime(){
        return time;
    }

    @Override
    public IIngredient getRecipeOutput() {
        return result;
    }

    @Override
    public boolean matches(ItemStack[] barrelInventory) {
        if (barrelInventory[0] == null) {
            return false;
        }

        if (!this.ingredients.getTalisman().equals(barrelInventory[0].getItem().getRegistryName())) {
            return false;
        }
        for (IIngredient ing : this.getIngredients().getIngredients()) {
            for (int i = 1; i < barrelInventory.length; i++) {
                ItemStack itemStack = barrelInventory[i];
                if (itemStack == null) {
                    continue;
                }
                if (ing.matches(CraftTweakerMC.getIItemStack(itemStack))) {
                    return ing.getAmount() <= itemStack.getCount();
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public RecipeIngredients getIngredients() {
        return ingredients;
    }
}
