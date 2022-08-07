package com.goodtime.bruaket.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class FuzzyBruaketRecipe {

    private final RecipeIngredients ingredients;

    private final ArrayList<IBruaketRecipe> possibleRecipes;

    public FuzzyBruaketRecipe(RecipeIngredients ingredients, ArrayList<IBruaketRecipe> possibleRecipes) {
        this.ingredients = ingredients;
        this.possibleRecipes = possibleRecipes;
    }

    public RecipeIngredients getIngredients() {
        return ingredients;
    }

    public ArrayList<IBruaketRecipe> getPossibleRecipes() {
        return possibleRecipes;
    }

    /**
     * Matches an acceptable recipe from a list of possible recipes
     * @param barrelInventory item stacks of the barrel inventory. The array length must be 10,
     *                        and the first element must be the item stack of talisman.
     *                        Elements of this array can be null.
     * @return a @{@link IBruaketRecipe} if there is a match, otherwise null
     */
    public IBruaketRecipe matches(ItemStack[] barrelInventory) {
        for (IBruaketRecipe recipe : this.possibleRecipes) {
            if (recipe.matches(barrelInventory)) {
                return recipe;
            }
        }
        return null;
    }
}
