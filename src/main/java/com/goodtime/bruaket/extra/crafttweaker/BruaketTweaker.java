package com.goodtime.bruaket.extra.crafttweaker;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.init.ItemInitializer;
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
        CraftTweaker.LATE_ACTIONS.add(new OrdinaryRecipe(barrel, output, ingredients, talisman, time));
    }

    @ZenMethod
    public static void addSmeltingRecipe(IIngredient ingredient, IIngredient output){
        CraftTweaker.LATE_ACTIONS.add(new SmeltingRecipe(ingredient, output));
    }

    @ZenMethod
    public static void addSmeltingRecipe(String barrel, IIngredient ingredient, IIngredient output){
        CraftTweaker.LATE_ACTIONS.add(new SmeltingRecipe(barrel, ingredient, output));
    }

    private static class SmeltingRecipe implements IAction{

        private final ResourceLocation barrel;

        private final IIngredient ingredient;

        private final IIngredient output;


        public SmeltingRecipe(IIngredient ingredient, IIngredient output) {
            this.barrel = ItemInitializer.nether_barrel.getRegistryName();
            this.ingredient = CraftTweakerMC.getIItemStackWildcardSize(CraftTweakerMC.getItemStack(ingredient)).amount(1);
            this.output = output;
        }

        public SmeltingRecipe(String barrel, IIngredient ingredient, IIngredient output) {
            this.barrel = new ResourceLocation(barrel);
            this.ingredient = CraftTweakerMC.getIItemStackWildcardSize(CraftTweakerMC.getItemStack(ingredient)).amount(1);
            this.output = output;
        }

        @Override
        public void apply() {
            BruaketSmeltingRecipe smeltingRecipe = new BruaketSmeltingRecipe(ingredient, output);

            IIngredient handleResult = handleOreDict(ingredient);

            if(handleResult != null){
                FuzzyRecipeList fuzzyRecipeList = RecipeListManager.INSTANCE.getFuzzyRecipeList(barrel);
                if(fuzzyRecipeList == null){
                    fuzzyRecipeList = new FuzzyRecipeList();
                    RecipeListManager.INSTANCE.putFuzzyRecipeList(barrel, fuzzyRecipeList);
                }
                fuzzyRecipeList.addRecipe(handleResult, smeltingRecipe);
            }else {
                RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
                if (recipeList == null){
                    recipeList = new RecipeList();
                    RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);
                }
                recipeList.addRecipe(smeltingRecipe);
            }
        }

        @Override
        public String describe() {
            return "Adding Bruaket smelting recipe for " + output.toString();
        }
    }

    private static class OrdinaryRecipe implements IAction {
        private final ResourceLocation barrel;
        private final ResourceLocation talisman;
        private final IIngredient output;
        private final IIngredient[] ingredients;
        private final int time;

        private OrdinaryRecipe(String barrel, IIngredient output, IIngredient[] ingredients, String talisman, int time){
            this.barrel = new ResourceLocation(barrel);
            this.output = output;
            this.ingredients = ingredients;
            this.talisman = new ResourceLocation(talisman);
            this.time = time;
        }

        @Override
        public void apply () {
            BruaketOrdinaryRecipe recipe = new BruaketOrdinaryRecipe(barrel, output, time, new RecipeIngredients(talisman, ingredients));
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

        @Override
        public String describe () {
            return "Adding Bruaket ordinary recipe for " + output.toString();
        }
    }

    private static IIngredient handleOreDict(IIngredient ingredient){
        ItemStack itemStack = CraftTweakerMC.getItemStack(ingredient);
        int[] ids = OreDictionary.getOreIDs(itemStack);
        if(ids.length != 0){
            return CraftTweakerMC.getIItemStackWildcardSize(OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0));
        }
        return null;
    }

}
