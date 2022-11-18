package com.goodtime.bruaket.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

/**
 * @author SuHao
 */
public class FuzzyBruaketRecipe {
    private final ArrayList<IBruaketRecipe> possibleRecipes = new ArrayList<>();

    public FuzzyBruaketRecipe(IBruaketRecipe bruaketRecipe) {
        this.possibleRecipes.add(bruaketRecipe);
    }

    public void addRecipe(IBruaketRecipe bruaketRecipe){
        this.possibleRecipes.add(bruaketRecipe);
    }

    public ArrayList<IBruaketRecipe> getPossibleRecipes() {
        return possibleRecipes;
    }

    /**
     * Matches an acceptable recipe from a list of possible recipes
     * @param barrelInventory item stacks of the barrel inventory. The array length must be 9.
     *                        Elements of this array can be null.
     * @return a @{@link IBruaketRecipe} if there is a match, otherwise null
     */
    public IBruaketRecipe matches(ResourceLocation talisman, NonNullList<ItemStack> barrelInventory) {
        for (IBruaketRecipe recipe : this.possibleRecipes) {
            if (recipe.matches(talisman, barrelInventory)) {
                return recipe;
            }
        }
        return null;
    }
}
