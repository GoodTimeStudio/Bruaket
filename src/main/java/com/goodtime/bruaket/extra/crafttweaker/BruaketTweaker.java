package com.goodtime.bruaket.extra.crafttweaker;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeList;
import com.goodtime.bruaket.recipe.RecipeListManager;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods."+ Bruaket.MODID+".barrel")
public class BruaketTweaker {

    @ZenMethod
    public static void addRecipe(IIngredient output, String barrel, IIngredient[] ingredients, String talisman, int time){
        CraftTweaker.LATE_ACTIONS.add(new Add(barrel, output, ingredients, talisman, time));
    }

    private static class Add implements IAction {
        private final ResourceLocation barrel;
        private final ResourceLocation talisman;
        private final IIngredient output;
        private final IIngredient[] ingredients;
        private final int time;

        private Add(String barrel, IIngredient output, IIngredient[] ingredients, String talisman, int time){
            this.barrel = new ResourceLocation(barrel);
            this.output = output;
            this.ingredients = ingredients;
            this.talisman = new ResourceLocation(talisman);
            this.time = time;
        }

        @Override
        public void apply () {
            BruaketRecipe recipe = new BruaketRecipe(barrel, output, time, new RecipeIngredients(talisman, ingredients));
            RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
            if (recipeList == null) {
                recipeList = new RecipeList();
                RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);
            }
            recipeList.addRecipe(recipe);
        }

        @Override
        public String describe () {
            return "Adding Bruaket recipe for " + output.toString();
        }
    }

}
