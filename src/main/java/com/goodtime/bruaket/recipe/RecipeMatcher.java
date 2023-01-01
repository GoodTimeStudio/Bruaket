package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class RecipeMatcher {

    public static @NotNull ArrayList<IBruaketRecipe> SmeltingRecipeMatch(IBarrelTile tile, ResourceLocation barrel, NonNullList<ItemStack> barrelInventory){
        ArrayList<IBruaketRecipe> recipes = new ArrayList<>();

        for (ItemStack itemStack : barrelInventory) {

            if(itemStack.isEmpty()) continue;

            ItemStack handleResult = handleOreDict(itemStack);
            RecipeIngredients recipeIngredients = new RecipeIngredients(null, CraftTweakerMC.getIIngredient(handleResult).amount(1));
            FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);

            if(fuzzyRecipeList == null) continue;

            FuzzyBruaketRecipe fuzzyRecipe = fuzzyRecipeList.matches(recipeIngredients);

            if(fuzzyRecipe == null) {
                tile.drop(itemStack, itemStack.getCount(), true);
                return recipes;
            }

            if(fuzzyRecipe.getPossibleRecipesCount() == 1){
                recipes.add(fuzzyRecipe.getPossibleRecipe(0));
            }else {
                IBruaketRecipe recipe = fuzzyRecipe.matches(null, itemStack);

                if(recipe == null) continue;

                recipes.add(recipe);
            }
        }

        return recipes;
    }


    public static IBruaketRecipe OrdinaryRecipeMatch(ResourceLocation barrel, ResourceLocation talisman, NonNullList<ItemStack> barrelInventory){
        HashSet<IIngredient> ingredients = new HashSet<>();

        for (ItemStack itemStack : barrelInventory) {
            if(itemStack.isEmpty()) continue;
            ItemStack handleResult = handleOreDict(itemStack);
            ingredients.add(CraftTweakerMC.getIIngredient(handleResult).amount(1));
        }

        RecipeIngredients recipeIngredients = new RecipeIngredients(talisman, ingredients);

        ItemStack[] inventory = barrelInventory.toArray(new ItemStack[0]);

        FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
        if(fuzzyRecipeList != null){
            FuzzyBruaketRecipe fuzzyRecipe = fuzzyRecipeList.matches(recipeIngredients);
            if(fuzzyRecipe != null){
                if(fuzzyRecipe.getPossibleRecipesCount() == 1){
                    return fuzzyRecipe.getPossibleRecipe(0);
                }
                return fuzzyRecipe.matches(talisman, inventory);
            }
        }
        return null;
    }



    private static ItemStack handleOreDict(ItemStack itemStack){
        int[] ids = OreDictionary.getOreIDs(itemStack);
        if(ids.length != 0){
            return OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0);
        }
        return itemStack;
    }

}
