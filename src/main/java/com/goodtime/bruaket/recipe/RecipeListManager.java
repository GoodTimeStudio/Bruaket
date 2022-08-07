package com.goodtime.bruaket.recipe;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

public class RecipeListManager {

    public static final RecipeListManager INSTANCE = new RecipeListManager();

    private final HashMap<ResourceLocation, RecipeList> recipeListInstancesMap;

    private RecipeListManager() {
        recipeListInstancesMap = new HashMap<>();
    }

    @Nullable
    public RecipeList getRecipeList(ResourceLocation barrel) {
        return recipeListInstancesMap.get(barrel);
    }

    public void putRecipeList(ResourceLocation barrel, RecipeList recipeList) {
        recipeListInstancesMap.put(barrel, recipeList);
    }
}
