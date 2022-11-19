package com.goodtime.bruaket.recipe;

import javax.annotation.Nullable;
import java.util.HashMap;

public class RecipeList  {

    private final HashMap<RecipeIngredients, IBruaketRecipe> recipesMap = new HashMap<>();

    public void addRecipe (IBruaketRecipe recipe) {
        recipesMap.put(recipe.getIngredients(), recipe);
    }
    @Nullable
    public IBruaketRecipe matches(RecipeIngredients ingredients) {
        return recipesMap.get(ingredients);
    }

}
