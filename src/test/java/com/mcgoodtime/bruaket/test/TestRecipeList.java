package com.mcgoodtime.bruaket.test;

import com.goodtime.bruaket.init.ItemInitializer;
import com.goodtime.bruaket.recipe.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
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
                CraftTweakerMC.getIIngredient(Items.BOAT),
                100,
                new RecipeIngredients(
                        ItemInitializer.stone_talisman.getRegistryName(),
                        new IIngredient[] {
                                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.ARROW, 1)),
                                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.APPLE, 1))
                        }
                )
        );

        NonNullList<ItemStack> barrelInventory = NonNullList.withSize(9, ItemStack.EMPTY);

        barrelInventory.set(0, new ItemStack(Items.ARROW, 1));
        barrelInventory.set(1, new ItemStack(Items.APPLE, 1));

        ResourceLocation barrel = ItemInitializer.wooden_barrel.getRegistryName();
        ResourceLocation talisman = ItemInitializer.stone_talisman.getRegistryName();

        RecipeList recipeList = new RecipeList();
        recipeList.addRecipe(recipe);
        RecipeListManager.INSTANCE.putRecipeList(barrel, recipeList);

        IBruaketRecipe bruaketRecipe = RecipeMatcher.match(barrel,  talisman, barrelInventory);
        assertNotNull(bruaketRecipe);
    }

    @Test
    public void testFUzzyRecipeList(){

        OreDictionary.registerOre("ingotIron", Items.COAL);

        IIngredient[] ingredients1 =  new IIngredient[] {
                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.COAL, 1)),
                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.APPLE, 1))
        };

        IIngredient[] ingredients2 =  new IIngredient[] {
                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.IRON_INGOT, 1)),
                CraftTweakerMC.getIItemStackWildcardSize(new ItemStack(Items.APPLE, 1))
        };

        BruaketRecipe recipe1 = new BruaketRecipe(
                Objects.requireNonNull(ItemInitializer.wooden_barrel.getRegistryName()),
                CraftTweakerMC.getIIngredient(Items.DIAMOND),
                100,
                new RecipeIngredients(
                        ItemInitializer.stone_talisman.getRegistryName(),
                        ingredients1
                )
        );

        BruaketRecipe recipe2 = new BruaketRecipe(
                Objects.requireNonNull(ItemInitializer.wooden_barrel.getRegistryName()),
                CraftTweakerMC.getIIngredient(Items.GOLD_INGOT),
                100,
                new RecipeIngredients(
                        ItemInitializer.stone_talisman.getRegistryName(),
                        ingredients2
                )
        );

        NonNullList<ItemStack> barrelInventory = NonNullList.withSize(9, ItemStack.EMPTY);

        barrelInventory.set(0, new ItemStack(Items.APPLE, 1));
        barrelInventory.set(1, new ItemStack(Items.COAL, 1));

        ResourceLocation barrel = ItemInitializer.wooden_barrel.getRegistryName();
        ResourceLocation talisman = ItemInitializer.stone_talisman.getRegistryName();

        handleIngredient(ingredients1);
        handleIngredient(ingredients2);

        FuzzyRecipeList fuzzyRecipeList = new FuzzyRecipeList();
        fuzzyRecipeList.addRecipe(ingredients1, talisman, recipe1);
        fuzzyRecipeList.addRecipe(ingredients2, talisman, recipe2);
        RecipeListManager.INSTANCE.putFuzzyRecipeList(barrel, fuzzyRecipeList);

        IBruaketRecipe bruaketRecipe = RecipeMatcher.match(barrel, talisman, barrelInventory);

        assertNotNull(bruaketRecipe);

        System.out.println(bruaketRecipe.getRecipeOutput());

    }

    private void handleIngredient(IIngredient[] ingredients){
        for (int i = 0; i < ingredients.length; i++) {
            ItemStack itemStack = CraftTweakerMC.getItemStack(ingredients[i]);
            int[] ids = OreDictionary.getOreIDs(itemStack);
            if(ids.length != 0){
                ingredients[i] = CraftTweakerMC.getIItemStackWildcardSize(OreDictionary.getOres(OreDictionary.getOreName(ids[0])).get(0));
            }
        }
    }

}
