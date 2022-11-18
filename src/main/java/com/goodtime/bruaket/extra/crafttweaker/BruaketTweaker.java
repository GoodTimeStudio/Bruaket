package com.goodtime.bruaket.extra.crafttweaker;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.*;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
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
            boolean useOreDic = false;

            for (int i = 0; i < ingredients.length; i++) {
                IIngredient handleResult = handleOreDict(ingredients[i]);
                ingredients[i] = CraftTweakerMC.getIItemStackWildcardSize(CraftTweakerMC.getItemStack(ingredients[i]));
                if(handleResult != null){
                    useOreDic = true;
                    ingredients[i] = handleResult;
                }
            }

            if(useOreDic){
                FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
                if(fuzzyRecipeList == null){
                    fuzzyRecipeList = new FuzzyRecipeList();
                    RecipeListManager.INSTANCE.putFuzzyRecipeList(barrel, fuzzyRecipeList);
                }
                fuzzyRecipeList.addRecipe(ingredients, talisman, recipe);
            } else {
                RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
                if (recipeList == null) {
                    recipeList = new RecipeList();
                    RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);
                }
                recipeList.addRecipe(recipe);
            }
        }

        private IIngredient handleOreDict(IIngredient ingredient){
            ItemStack itemStack = CraftTweakerMC.getItemStack(ingredient);
            int[] ids = OreDictionary.getOreIDs(itemStack);
            if(ids.length != 0){
                return CraftTweakerMC.getIItemStackWildcardSize(OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0));
            }
            return null;
        }


        @Override
        public String describe () {
            return "Adding Bruaket recipe for " + output.toString();
        }
    }

}
