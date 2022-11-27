package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;

public class RecipeList  {

    private final HashMap<RecipeIngredients, IBruaketRecipe> recipesMap = new HashMap<>();

    public void addRecipe (IIngredient[] ingredients, ResourceLocation talisman, IBruaketRecipe recipe) {
        recipesMap.put(new RecipeIngredients(talisman, ingredients), recipe);
    }

    public void addRecipe(IIngredient ingredient, ResourceLocation talisman, IBruaketRecipe recipe){
        HashSet<IIngredient> smeltingIngredient = new HashSet<>();
        smeltingIngredient.add(ingredient);
        recipesMap.put(new RecipeIngredients(talisman, smeltingIngredient), recipe);
    }


    @Nullable
    public IBruaketRecipe matches(RecipeIngredients ingredients) {
        return recipesMap.get(ingredients);
    }

}
