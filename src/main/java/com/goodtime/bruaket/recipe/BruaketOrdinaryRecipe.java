package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author SuHao
 * @date 2022/8/8
 */
public class BruaketOrdinaryRecipe implements IBruaketRecipe {

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

    public BruaketOrdinaryRecipe(@Nonnull ResourceLocation barrel, @Nonnull IIngredient result, int time, @Nonnull RecipeIngredients ingredients) {
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
    public boolean matches(ResourceLocation talisman, ItemStack... barrelInventory) {
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
