package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class FuzzyRecipeList {

    private final HashMap<RecipeIngredients, FuzzyBruaketRecipe> fuzzyRecipesMap = new HashMap<>();

    public void addRecipe (IIngredient[] ingredients, ResourceLocation talisman, IBruaketRecipe bruaketRecipe) {
        RecipeIngredients recipeIngredients = new RecipeIngredients(talisman, new HashSet<>(Arrays.asList(ingredients)));

        if(fuzzyRecipesMap.containsKey(recipeIngredients)){
            fuzzyRecipesMap.get(recipeIngredients).addRecipe(bruaketRecipe);
        }else {
            fuzzyRecipesMap.put(recipeIngredients, new FuzzyBruaketRecipe(bruaketRecipe));
        }
    }
    @Nullable
    public FuzzyBruaketRecipe matches(RecipeIngredients ingredients) {
        return fuzzyRecipesMap.get(ingredients);
    }


}
