package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IBruaketRecipe {

    /**
     * Get the output item of this recipe
     * @return output item
     */
    IIngredient getRecipeOutput ();

    /***
     * Checks if the given tailsman and crafting ingredients match this recipe
     * @param barrelInventory item stacks of the barrel inventory. The array length must be 10,
     *                        and the first element must be the item stack of talisman.
     *                        Elements of this array can be null.
     * @return true if match
     */
    boolean matches(ItemStack[] barrelInventory);

    /**
     * Get the registry name of the barrel used to craft this recipe
     * @return barrel name
     */
    ResourceLocation getBarrel();

    /**
     * Get the required ingredients to craft the item
     * @return list of ingredients
     */
    RecipeIngredients getIngredients ();

    /**
     * Get the time needed to craft the item, in ticks
     * @return time in ticks
     */
    long getTime();
}
