package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.BruaketSmeltingRecipe;
import com.goodtime.bruaket.recipe.RecipeList;
import com.goodtime.bruaket.recipe.RecipeListManager;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestBruaketSmeltingRecipe {

    @BeforeAll
    public static void init() {
        Bootstrap.register();
        ItemInitializer.registerItems();
    }

    @Test
    public void testSmeltingRecipe(){
        IBruaketRecipe smeltingRecipe = new BruaketSmeltingRecipe(CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.BEEF, 1)), CraftTweakerMC.getIIngredient(Items.COAL));

        IBruaketRecipe smeltingRecipe2 = new BruaketSmeltingRecipe(CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.APPLE, 1)), CraftTweakerMC.getIIngredient(Items.BREAD));


        NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

        inventory.set(0, new ItemStack(Items.BEEF, 1));

        inventory.set(1, new ItemStack(Items.APPLE, 1));


        ResourceLocation barrel = ItemInitializer.nether_barrel.getRegistryName();

        RecipeList recipeList = new RecipeList();
        recipeList.addRecipe(CraftTweakerMC.getIIngredient(new ItemStack(Items.BEEF, 1)).amount(1), null, smeltingRecipe);
        recipeList.addRecipe(CraftTweakerMC.getIIngredient(new ItemStack(Items.APPLE, 1)).amount(1), null, smeltingRecipe2);

        RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);

        ArrayList<IBruaketRecipe> recipes = RecipeMatcher.SmeltingRecipeMatch(barrel, inventory);

        assertFalse(recipes.isEmpty());

        recipes.forEach(recipe -> System.out.println(recipe.getRecipeOutput()));


    }


}
