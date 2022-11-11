package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.recipe.RecipeIngredients;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestRecipeIngredients {

    @Test
    void testEquality() {
        // Init Items
        Bootstrap.register();

        // test empty ingredients
        RecipeIngredients r1 = new RecipeIngredients(
                new ResourceLocation("foobar"),
                new HashSet<>());
        RecipeIngredients r2 = new RecipeIngredients(
                new ResourceLocation("foobar"),
                new HashSet<>());

        assertEquals(r1, r2);

        HashSet<IIngredient> s1 = new HashSet<>();
        s1.add(CraftTweakerMC.getIIngredient(new ItemStack(Items.APPLE, 3)));
        r1 = new RecipeIngredients(
                new ResourceLocation("foobar"),
                s1);

        assertNotEquals(r1, r2);

        HashSet<IIngredient> s2 = new HashSet<>();
        s2.add(CraftTweakerMC.getIIngredient(new ItemStack(Items.APPLE, 3)));
        s2.add(CraftTweakerMC.getIIngredient(new ItemStack(Items.DIAMOND, 10)));
        r2 = new RecipeIngredients(
                new ResourceLocation("foobar"),
                s2);

        s1.add(CraftTweakerMC.getIIngredient(new ItemStack(Items.DIAMOND, 10)));

        assertEquals(r1, r2);
    }
}
