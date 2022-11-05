package com.goodtime.bruaket.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.util.CraftTweakerHacks;
import crafttweaker.mc1120.util.CraftTweakerPlatformUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;
import java.util.HashMap;

public class RecipeList  {

    private final HashMap<RecipeIngredients, IBruaketRecipe> recipesMap = new HashMap<>();
    private final HashMap<RecipeIngredients, FuzzyBruaketRecipe> fuzzyRecipesMap = new HashMap<>();
    public void addRecipe (IBruaketRecipe recipe) {
        recipesMap.put(recipe.getIngredients(), recipe);
    }
    @Nullable
    public IBruaketRecipe matches(RecipeIngredients ingredients) {
        return recipesMap.get(ingredients);
    }

    /**
     * Match a recipe using barrel's inventory.
     * @param barrelInventory item stacks of the barrel inventory. The array length must be 10,
     *                        and the first item must be the item stack of talisman.
     *                        Elements of this array can be null.
     * @return a @{@link IBruaketRecipe} if there is a match, otherwise null
     */
    public IBruaketRecipe matches(ItemStack[] barrelInventory) {
        if (barrelInventory.length != 10) {
            throw new IllegalArgumentException("Length of the item stacks must be 10");
        }
        HashSet<IIngredient> ingredients = new HashSet<>();
        for (int i = 1; i < barrelInventory.length; i++) {
            ItemStack itemStack = barrelInventory[i];
            if(itemStack != null){
                ingredients.add(CraftTweakerMC.getIIngredient(itemStack));
            }
        }

        IBruaketRecipe recipe = matches(new RecipeIngredients(barrelInventory[0].getItem().getRegistryName(), ingredients));
        if (recipe != null && recipe.matches(barrelInventory)) {
            return recipe;
        }

        // TODO: ore dict - fuzzy match
       // OreDictionary.getOreIDs()
        return null;
    }

   /* public static RecipeIngredients getFuzzyRecipeIngredients(RecipeIngredients recipeIngredients) {
        for (IIngredient ing : recipeIngredients.getIngredients()) {

        }
        RecipeIngredients ret = new RecipeIngredients(recipeIngredients.getTalisman());
    }*/
}
