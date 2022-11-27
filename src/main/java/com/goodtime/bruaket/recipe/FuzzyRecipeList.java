package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class FuzzyRecipeList {

    private final HashMap<RecipeIngredients, FuzzyBruaketRecipe> fuzzyRecipesMap = new HashMap<>();

    public void addRecipe(IIngredient ingredient, ResourceLocation talisman, BruaketSmeltingRecipe smeltingRecipe){
        HashSet<IIngredient> smeltingIngredient = new HashSet<>();
        smeltingIngredient.add(ingredient);
        RecipeIngredients recipeIngredients = new RecipeIngredients(talisman, smeltingIngredient);

        if(fuzzyRecipesMap.containsKey(recipeIngredients)){
            fuzzyRecipesMap.get(recipeIngredients).addRecipe(smeltingRecipe);
        }else {
            fuzzyRecipesMap.put(recipeIngredients, new FuzzyBruaketRecipe(smeltingRecipe));
        }

    }

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
