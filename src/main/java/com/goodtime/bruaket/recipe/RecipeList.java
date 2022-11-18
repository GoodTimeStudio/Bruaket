package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.entity.bruaket.BarrelTileEntity;
import com.goodtime.bruaket.items.Talisman;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;

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
