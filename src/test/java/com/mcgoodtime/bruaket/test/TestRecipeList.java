package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeList;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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
                        ItemInitializer.fire_talisman.getRegistryName(),
                        new IIngredient[] {
                                CraftTweakerMC.getIIngredient(new ItemStack(Items.APPLE, 3))
                        }
                )
        );

        ItemStack[] barrelInventory = new ItemStack[10];

        RecipeList rl = new RecipeList();
        rl.addRecipe(recipe);

        barrelInventory[0] = new ItemStack(ItemInitializer.fire_talisman);

        barrelInventory[2] = new ItemStack(Items.APPLE, 2);

        assertNull(rl.matches(barrelInventory));

        barrelInventory[2] = new ItemStack(Items.APPLE, 3);

        assertNotNull(rl.matches(barrelInventory));
    }

}
