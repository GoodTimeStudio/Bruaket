package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;

public class RecipeMatcher {

    public static IBruaketRecipe match(ResourceLocation barrel, ResourceLocation talisman, NonNullList<ItemStack> barrelInventory){
        HashSet<IIngredient> ingredients = new HashSet<>();
        boolean useOreDic = false;

        for (ItemStack itemStack : barrelInventory) {
            if (!itemStack.isEmpty()) {
                ItemStack handleResult = handleOreDict(itemStack);
                if (handleResult != null) {
                    useOreDic = true;
                    ingredients.add(CraftTweakerMC.getIItemStackWildcardSize(handleResult));
                } else {
                    ingredients.add(CraftTweakerMC.getIItemStackWildcardSize(itemStack));
                }
            }
        }

        RecipeIngredients recipeIngredients = new RecipeIngredients(talisman, ingredients);
        
        if(useOreDic){
            FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
            if(fuzzyRecipeList != null){
                FuzzyBruaketRecipe fuzzyRecipe = fuzzyRecipeList.matches(recipeIngredients);
                if(fuzzyRecipe != null){
                    return fuzzyRecipe.matches(talisman, barrelInventory);
                }
            }
        }else {
            RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
            if(recipeList != null){
                IBruaketRecipe bruaketRecipe = recipeList.matches(recipeIngredients);
                if(bruaketRecipe != null && bruaketRecipe.matches(talisman, barrelInventory)){
                    return bruaketRecipe;
                }
            }
        }
        return null;
    }

    private static ItemStack handleOreDict(ItemStack itemStack){
        int[] ids = OreDictionary.getOreIDs(itemStack);
        if(ids.length != 0){
            return OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0);
        }
        return null;
    }

}
