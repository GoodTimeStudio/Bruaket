package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RecipeMatcher {

    public static ArrayList<IBruaketRecipe> SmeltingRecipeMatch(ResourceLocation barrel, NonNullList<ItemStack> barrelInventory){
        ArrayList<IBruaketRecipe> recipes = new ArrayList<>();

        for (ItemStack itemStack : barrelInventory) {

            ItemStack handleResult = handleOreDict(itemStack);
            RecipeIngredients recipeIngredients;

            if(handleResult != null){
                recipeIngredients = new RecipeIngredients(null, handleResult);
                FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
                if(fuzzyRecipeList != null){
                    FuzzyBruaketRecipe fuzzyRecipe = fuzzyRecipeList.matches(recipeIngredients);
                    if(fuzzyRecipe != null){
                        recipes.add(fuzzyRecipe.matches(null, itemStack));
                    }
                }
            }else {
                recipeIngredients = new RecipeIngredients(null, itemStack);
                RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
                if(recipeList != null){
                    IBruaketRecipe bruaketRecipe = recipeList.matches(recipeIngredients);
                    if(bruaketRecipe != null && bruaketRecipe.matches(null, itemStack)){
                        recipes.add(bruaketRecipe);
                    }
                }
            }
        }

        return recipes;
    }


    public static IBruaketRecipe OrdinaryRecipeMatch(ResourceLocation barrel, ResourceLocation talisman, NonNullList<ItemStack> barrelInventory){
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

        ItemStack[] inventory = barrelInventory.toArray(new ItemStack[0]);

        if(useOreDic){
            FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
            if(fuzzyRecipeList != null){
                FuzzyBruaketRecipe fuzzyRecipe = fuzzyRecipeList.matches(recipeIngredients);
                if(fuzzyRecipe != null){
                    return fuzzyRecipe.matches(talisman, inventory);
                }
            }
        }else {
            RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
            if(recipeList != null){
                IBruaketRecipe bruaketRecipe = recipeList.matches(recipeIngredients);
                if(bruaketRecipe != null && bruaketRecipe.matches(talisman, inventory)){
                    return bruaketRecipe;
                }
            }
        }
        return null;
    }




    private static ItemStack handleOreDict(ItemStack itemStack){
        if(itemStack.isEmpty()){
            return null;
        }
        int[] ids = OreDictionary.getOreIDs(itemStack);
        if(ids.length != 0){
            return OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0);
        }
        return null;
    }

}
