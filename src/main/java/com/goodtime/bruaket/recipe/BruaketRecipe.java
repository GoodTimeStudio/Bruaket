package com.goodtime.bruaket.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author SuHao
 * @date 2022/8/8
 */
public class BruaketRecipe implements IBruaketRecipe {

    /**
     * Required Barrel
     */
    private final ResourceLocation barrel;

    private final RecipeIngredients ingredients;

    /**
     * Recipe result
     */
    private final IIngredient result;

    /**
     * Recipe required time
     */
    private final int time;

    public BruaketRecipe(@Nonnull ResourceLocation barrel, @Nonnull IIngredient result, int time, @Nonnull RecipeIngredients ingredients) {
        this.barrel = barrel;
        this.ingredients = ingredients;
        this.result = result;
        this.time = time;
    }

    @Override
    public ResourceLocation getBarrel() {
        return this.barrel;
    }

    @Override
    public int getTime(){
        return time;
    }

    @Override
    public IIngredient getRecipeOutput() {
        return result;
    }


    /**
     * Recipe matches
     *
     * @param barrelInventory Barrel`s inventory item stack
     * @return boolean
     */
    @Override
    public boolean matches(ResourceLocation talisman, NonNullList<ItemStack> barrelInventory) {
        if (!this.ingredients.getTalisman().equals(talisman)) {
            return false;
        }
        int count = 0;
        for (IIngredient ing : this.getIngredients().getIngredients()) {
            for (ItemStack itemStack : barrelInventory) {
                if (itemStack.isEmpty()) continue;
                if (ing.matches(CraftTweakerMC.getIItemStack(itemStack)) && ing.getAmount() <= itemStack.getCount() ) {
                    count ++;
                }
            }
        }
        return count == this.getIngredients().getIngredients().size();
    }

    @Override
    public RecipeIngredients getIngredients() {
        return ingredients;
    }
}
