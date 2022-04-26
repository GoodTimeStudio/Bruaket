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
import util.ItemUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BruaketRecipe implements IRecipe {

    //合成名称
    private final ResourceLocation name;

    //合成所用的桶
    private final String barrel;

    //合成所需原料
    private final List<IngredientStack> ingredients = new ArrayList<>();

    //合成所需符咒
    private final String talisman;

    //合成产出
    private final ItemStack result;

    //合成所需时间
    private final int time;


    public BruaketRecipe(String name, String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe){
        this(new ResourceLocation(Bruaket.MODID, name), barrel, result, talisman,time, recipe);
    }

    public BruaketRecipe(ResourceLocation name, String barrel, @Nonnull ItemStack result, String talisman,int time, List<IngredientStack> ingredients){
        this.name = name;
        this.barrel = barrel;
        this.ingredients.addAll(ingredients);
        this.talisman = talisman;
        this.result = result;
        this.time = time;
    }

    public BruaketRecipe(ResourceLocation name,  String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe){
        this.name = name;
        this.barrel = barrel;
        this.talisman = talisman;
        this.result = result.copy();
        this.time = time;
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
    public int getIngredientsSize(){
        return ingredients.size();
    }

    @Override
    public long getTime(){
        return time;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public String getTailsman() {
        return talisman;
    }

    @Override
    public String getBarrel(){return barrel;}

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

    @Override
    public boolean contains(ItemStack itemStack) {
        AtomicBoolean contains = new AtomicBoolean(false);
        for (IngredientStack ingredient : ingredients) {
            ItemStack[] itemStacks = ingredient.getMatchingStacks();
            for (int i = 0; i < itemStacks.length; i++) {
                if(ItemUtils.areStacksEqualIgnoreSize(itemStacks[i], itemStack)){
                    contains.set(true);
                    break;
                }
            }
            if(contains.get()){
                break;
            }
        }
        return contains.get();
    }
}
