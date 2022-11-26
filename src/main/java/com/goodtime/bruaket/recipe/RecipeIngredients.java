package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * 配方成分
 *
 * @author SuHao
 * @date 2022/8/8
 */
public class RecipeIngredients {
    private final ResourceLocation talisman;

    private final HashSet<IIngredient> ingredients;

    public  RecipeIngredients(ResourceLocation talisman, ItemStack itemStack){
        this.talisman = talisman;
        this.ingredients = new HashSet<>();
        this.ingredients.add(CraftTweakerMC.getIItemStackWildcardSize(itemStack));
    }

    public RecipeIngredients(ResourceLocation talisman, HashSet<IIngredient> ingredients) {
        if (ingredients.size() > 9) {
            throw new IllegalArgumentException("Ingredients size must be less than or equal to 9");
        }
        this.talisman = talisman;
        this.ingredients = ingredients;
    }

    public RecipeIngredients(ResourceLocation talisman, IIngredient[] ingredients) {
        if (ingredients.length > 9) {
            throw new IllegalArgumentException("Ingredients size must be less than or equal to 9");
        }
        this.ingredients = new HashSet<>();
        this.ingredients.addAll(Arrays.asList(ingredients));
        this.talisman = talisman;
    }

    public ResourceLocation getTalisman() {
        return talisman;
    }

    public HashSet<IIngredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeIngredients that = (RecipeIngredients) o;

        if(talisman == null){
            return ingredients.equals(that.ingredients);
        }

        return talisman.equals(that.talisman) && ingredients.equals(that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talisman, ingredients);
    }
}
