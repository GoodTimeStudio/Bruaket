package com.goodtime.bruaket.extra.crafttweaker;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.IngredientStack;
import com.goodtime.bruaket.recipe.RecipeList;
import com.goodtime.bruaket.recipe.bruaket.IRecipe;
import crafttweaker.CraftTweakerAPI;
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

@ZenClass("mods."+ Bruaket.MODID+".Bruaket")
public class BruaketTweaker {

    @ZenMethod
    public static void addRecipe (String name, IItemStack output, IIngredient[] inputs) {
        CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), inputs, false));
    }

    @ZenMethod
    public static void removeRecipe (IItemStack output) {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void replaceRecipe (String name, IItemStack output, IIngredient[] inputs) {
        CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), inputs, true));
    }

    private static class Add extends Action {
        private final ResourceLocation name;
        private final ItemStack output;
        private final IIngredient[] ingredients;
        private final boolean replace;

        private Add (String name, ItemStack output, IIngredient[] ingredients, boolean replace) {
            super("GCT Recipe addition");
            this.name = new ResourceLocation(Bruaket.MODID, name);
            this.output = output;
            this.ingredients = ingredients;
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
            BruaketRecipe recipe = new BruaketRecipe(name, output, stacks);
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
            super("GCT Recipe removal");
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
