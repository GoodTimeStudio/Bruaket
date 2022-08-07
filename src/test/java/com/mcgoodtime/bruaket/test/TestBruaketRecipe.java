package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.BruaketRecipe;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

public class TestBruaketRecipe {

    @BeforeAll
    public static void init() {
        Bootstrap.register();
        ItemInitializer.registerItems();
    }

    @Test
    public void testNormalMatch() {
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
        assertFalse(recipe.matches(barrelInventory));

        barrelInventory[0] = new ItemStack(ItemInitializer.fire_talisman);
        // not enough item
        barrelInventory[2] = new ItemStack(Items.APPLE, 2);
        assertFalse(recipe.matches(barrelInventory));

        barrelInventory[2].setCount(3);
        assertTrue(recipe.matches(barrelInventory));
    }

    @Test
    public void testOreDictMatch() {
        BruaketRecipe recipe = new BruaketRecipe(
                Objects.requireNonNull(ItemInitializer.wooden_barrel.getRegistryName()),
                new MCOreDictEntry("gemDiamond").amount(2),
                100,
                new RecipeIngredients(
                        ItemInitializer.ultra_flamma_talisman.getRegistryName(),
                        new IIngredient[] {
                                new MCOreDictEntry("ingotIron").amount(9)
                        }
                )
        );

        ItemStack[] barrelInventory = new ItemStack[10];
        barrelInventory[0] = new ItemStack(ItemInitializer.ultra_flamma_talisman);
        barrelInventory[1] = new ItemStack(Items.IRON_INGOT, 9);
        assertTrue(recipe.matches(barrelInventory));
    }
}
