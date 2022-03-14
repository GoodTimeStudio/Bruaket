package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.bruaket.IRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BruaketRecipe implements IRecipe {

    private final List<IngredientStack> ingredients = new ArrayList<>();
    private final ItemStack result;
    private final ResourceLocation name;

    public BruaketRecipe(String name, @Nonnull ItemStack result, Object... recipe){
        this(new ResourceLocation(Bruaket.MODID, name), result, recipe);
    }

    public BruaketRecipe(ResourceLocation name, @Nonnull ItemStack result, List<IngredientStack> ingredients){
        this.result = result;
        this.name = name;
        this.ingredients.addAll(ingredients);
    }

    public BruaketRecipe(ResourceLocation name, @Nonnull ItemStack result, Object... recipe){
        this.result = result.copy();
        this.name = name;
        int i = 0;
        for (Object stack : recipe) {
            if (stack instanceof ItemStack) {
                ingredients.add(new IngredientStack((ItemStack) stack));
            } else if (stack instanceof Item) {
                ingredients.add(new IngredientStack((Item) stack));
            } else if (stack instanceof Ingredient) {
                ingredients.add(new IngredientStack((Ingredient) stack));
            } else if (stack instanceof String) {
                ingredients.add(new IngredientStack((String) stack));
            } else if (stack instanceof IngredientStack) {
                ingredients.add((IngredientStack) stack);
            } else if (stack instanceof Block) {
                ingredients.add(new IngredientStack((Block) stack));
            } else {
                Bruaket.logger.warn(String.format("Unknown ingredient type for recipe %s, skipped: ingredient %d, %s", name.toString(), i, stack.toString()));
            }
            i++;
        }
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public List<IngredientStack> getIngredients() {
        return ingredients;
    }

    @Override
    public int getIndex() {
        return RecipeList.instance.indexOf(this);
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public ItemStack onCrafted(EntityPlayer player, ItemStack output) {
        return output;
    }

    @Override
    public boolean craftable(EntityPlayer player, TileEntity craftingTable) {
        return true;
    }

    @Override
    public IRecipe addCondition(Condition predicate) {
        return IRecipe.super.addCondition(predicate);
    }
}
