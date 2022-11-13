package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestRecipeList {

    @BeforeAll
    public static void init() {
        Bootstrap.register();
        ItemInitializer.registerItems();
    }

    @Test
    public void testRecipeList(){
        BruaketRecipe recipe = new BruaketRecipe(
                Objects.requireNonNull(ItemInitializer.wooden_barrel.getRegistryName()),
                CraftTweakerMC.getIIngredient(Items.APPLE),
                100,
                new RecipeIngredients(
                        ItemInitializer.stone_talisman.getRegistryName(),
                        new IIngredient[] {
                                CraftTweakerMC.getIIngredient(new ItemStack(Items.BOOK, 1)),
                                CraftTweakerMC.getIIngredient(new ItemStack(Items.STICK, 1))
                        }
                )
        );

        NonNullList<ItemStack> barrelInventory = NonNullList.withSize(9, ItemStack.EMPTY);

        ResourceLocation barrel = ItemInitializer.wooden_barrel.getRegistryName();

        RecipeList recipeList = RecipeListManager.INSTANCE.getRecipeList(barrel);
        if (recipeList == null) {
            recipeList = new RecipeList();
            RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);
        }
        recipeList.addRecipe(recipe);

        barrelInventory.set(0, new ItemStack(Items.STICK, 1));
        assertNull(recipeList.matches(ItemInitializer.stone_talisman,barrelInventory));
        barrelInventory.set(1, new ItemStack(Items.BOOK, 1));
        IBruaketRecipe bruaketRecipe = recipeList.matches(ItemInitializer.stone_talisman, barrelInventory);
        assertNotNull(bruaketRecipe);
        System.out.println(bruaketRecipe.getRecipeOutput());
    }

}
