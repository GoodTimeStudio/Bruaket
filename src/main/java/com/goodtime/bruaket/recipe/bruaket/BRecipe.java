package com.goodtime.bruaket.recipe.bruaket;

import com.goodtime.bruaket.recipe.IngredientStack;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * This is an internal recipe interface not intended for overriding
 * or public use.
 */
public interface BRecipe {

    ItemStack getRecipeOutput ();

    List<IngredientStack> getIngredients ();

}
