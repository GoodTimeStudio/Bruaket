package com.goodtime.bruaket.extra.crafttweaker;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.IngredientStack;
import com.goodtime.bruaket.recipe.RecipeList;
import com.goodtime.bruaket.recipe.bruaket.IRecipe;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass("mods."+ Bruaket.MODID+".barrel")
public class BruaketTweaker {

    @ZenMethod
    public static void addRecipe(String name, IItemStack output, String barrel,  IIngredient[] inputs, String talisman, int time){
        CraftTweaker.LATE_ACTIONS.add(new Add(name, barrel, CraftTweakerMC.getItemStack(output), inputs, talisman,time, false));
    }

    @ZenMethod
    public static void removeRecipe (IItemStack output) {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void replaceRecipe (String name, String barrel,  IItemStack output, IIngredient[] inputs, String talisman, int time) {
        CraftTweaker.LATE_ACTIONS.add(new Add(name, barrel,  CraftTweakerMC.getItemStack(output), inputs, talisman, time, true ));
    }

    private static class Add extends Action {
        private final ResourceLocation name;
        private final String barrel;
        private final ItemStack output;
        private final IIngredient[] ingredients;
        private final String talisman;
        private final int time;
        private final boolean replace;

        private Add(String name, String barrel, ItemStack output, IIngredient[] ingredients, String talisman, int time, boolean replace){
            super("Bruaket Recipe addition");
            this.name = new ResourceLocation(Bruaket.MODID, name);
            this.barrel = barrel;
            this.output = output;
            this.ingredients = ingredients;
            this.talisman = talisman;
            this.time = time;
            this.replace = replace;
        }

        @Override
        public void apply () {
            if (this.replace) {
                if (RecipeList.instance.getRecipe(name) == null) {
                    CraftTweakerAPI.logError("Attempting to replace recipe " + name.toString() + " when it doesn't exist. Use addRecipe instead.");
                    return;
                }
            }
            List<IngredientStack> stacks = new ArrayList<>();
            for (IIngredient ingredient : ingredients) {
                stacks.add(new IngredientStack(CraftTweakerMC.getIngredient(ingredient), ingredient.getAmount()));
            }
            BruaketRecipe recipe = new BruaketRecipe(name, barrel, output, talisman, time, stacks);
            RecipeList.instance.addRecipe(recipe);
        }

        @Override
        public String describe () {
            return "Adding Bruaket recipe for " + output.toString();
        }
    }


    private static class Remove extends Action{

        private ItemStack output;

        private Remove (ItemStack stack) {
            super("Bruaket Recipe removal");
            this.output = stack;
        }

        @Override
        public String describe () {
            return "Removing " + output.getItem().getRegistryName().toString();
        }

        @Override
        public void apply () {
            IRecipe recipe = RecipeList.instance.getRecipeByOutput(output);
            if (recipe == null) {
                CraftTweakerAPI.logError("Invalid recipe for " + output.getItem().getRegistryName().toString());
            } else {
                RecipeList.instance.removeRecipe(recipe);
            }
        }
    }

}
