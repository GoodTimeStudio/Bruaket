package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.items.Talisman;
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

    //合成名称
    private final ResourceLocation name;

    //合成所需原料
    private final List<IngredientStack> ingredients = new ArrayList<>();

    //合成所需符咒
    private final Talisman talisman;

    //合成产出
    private final ItemStack result;


    public BruaketRecipe(String name, @Nonnull ItemStack result, Talisman talisman, Object... recipe){
        this(new ResourceLocation(Bruaket.MODID, name), result, talisman, recipe);
    }

    public BruaketRecipe(ResourceLocation name, @Nonnull ItemStack result, Talisman talisman, List<IngredientStack> ingredients){
        this.name = name;
        this.ingredients.addAll(ingredients);
        this.talisman = talisman;
        this.result = result;
    }

    public BruaketRecipe(ResourceLocation name, @Nonnull ItemStack result, Talisman talisman, Object... recipe){
        this.name = name;
        this.talisman = talisman;
        this.result = result.copy();
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
    public Talisman getTailsman() {
        return talisman;
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
