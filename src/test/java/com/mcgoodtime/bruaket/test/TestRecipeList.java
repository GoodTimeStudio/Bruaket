package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeList;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                CraftTweakerMC.getIIngredient(Items.DIAMOND),
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

        RecipeList rl = new RecipeList();
        rl.addRecipe(recipe);

//        barrelInventory.set(2, new ItemStack(Items.APPLE, 2));
//        assertNull(rl.matches(barrelInventory));

        barrelInventory.set(0, new ItemStack(Items.STICK, 1));
        barrelInventory.set(1, new ItemStack(Items.BOOK, 1));

        assertNotNull(rl.matches(ItemInitializer.stone_talisman, barrelInventory));
    }

}
