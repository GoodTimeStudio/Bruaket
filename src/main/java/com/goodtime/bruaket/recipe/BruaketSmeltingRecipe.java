package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class BruaketSmeltingRecipe implements IBruaketRecipe {

    private final IIngredient ingredient;

    private final IIngredient output;

    public BruaketSmeltingRecipe(IIngredient ingredient, IIngredient output) {
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public IIngredient getRecipeOutput() {
        return output;
    }

    @Override
    public boolean matches(ResourceLocation talisman, ItemStack... barrelInventory) {
        ItemStack itemStack = barrelInventory[0];
        return ingredient.matches(CraftTweakerMC.getIItemStack(itemStack)) && ingredient.getAmount() <= itemStack.getCount();
    }

    @Override
    public ResourceLocation getBarrel() {
        return ItemInitializer.nether_barrel.getRegistryName();
    }

    @Override
    public RecipeIngredients getIngredients() {
        HashSet<IIngredient> smeltingIngredient  = new HashSet<>();
        smeltingIngredient.add(ingredient);
        return new RecipeIngredients(null, smeltingIngredient);
    }

    @Override
    public int getTime() {
        return -1;
    }
}
