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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        System.out.println(new ResourceLocation("bruaket:wooden_barrel"));  //result: bruaket:wooden_barrel


        NonNullList<ItemStack> barrelInventory = NonNullList.withSize(10, ItemStack.EMPTY);

        // not enough item
        barrelInventory.set(1, new ItemStack(Items.APPLE, 2));
        assertFalse(recipe.matches(ItemInitializer.fire_talisman.getRegistryName(), barrelInventory));

        barrelInventory.get(1).setCount(3);
        assertTrue(recipe.matches(ItemInitializer.fire_talisman.getRegistryName(), barrelInventory));
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

        NonNullList<ItemStack> barrelInventory = NonNullList.withSize(9, ItemStack.EMPTY);

        barrelInventory.set(1, new ItemStack(Items.IRON_INGOT, 9));
        assertTrue(recipe.matches(ItemInitializer.ultra_flamma_talisman.getRegistryName(), barrelInventory));
    }
}
