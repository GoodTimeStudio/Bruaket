package com.goodtime.bruaket.recipe;


import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.recipe.bruaket.IRecipe;
import com.goodtime.bruaket.recipe.bruaket.IRecipeList;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import util.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class RecipeList implements IRecipeList {

    public static final RecipeList instance = new RecipeList();

    private final LinkedHashMap<ResourceLocation, IRecipe> RECIPE_LIST = new LinkedHashMap<>();

    private final LinkedHashMap<String, LinkedList<IRecipe>> DIVIDED_LIST = new LinkedHashMap<>();

    private ImmutableList<IRecipe> IMMUTABLE_COPY = null;


    @Override
    public Map<ResourceLocation, IRecipe> getRecipes () {
        return RECIPE_LIST;
    }

    @Override
    @Nullable
    public IRecipe getRecipeByOutput (ItemStack output) {
        for (IRecipe recipe : RECIPE_LIST.values()) {
            if (ItemUtils.areStacksEqualIgnoreSize(output, recipe.getRecipeOutput())) {
                return recipe;
            }
        }

        return null;
    }

    public LinkedList<IRecipe> getRecipeListByBarrelAndTalisMan(String barrel, String talisman){

        String splitter = talisman + "&" + barrel;

        LinkedList<IRecipe> recipes = DIVIDED_LIST.get(splitter);

        return recipes.isEmpty() ? null : recipes;
    }

    public LinkedList<IRecipe> getRecipeListByItem(ItemStack itemStack, LinkedList<IRecipe> currentRecipe){
        LinkedList<IRecipe> recipes = new LinkedList<>();
        if(currentRecipe != null){
            currentRecipe.forEach(recipe -> {
                if(recipe.contains(itemStack)){
                    recipes.add(recipe);
                }
            });

        }
        return recipes.isEmpty() ? null : recipes;

    }

    @Override
    public List<IRecipe> getRecipeList () {
        if (IMMUTABLE_COPY == null) {
            IMMUTABLE_COPY = ImmutableList.copyOf(RECIPE_LIST.values());
        }

        return IMMUTABLE_COPY;
    }

    @Override
    public IRecipe makeAndAddRecipe (String name, String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe) {
        BruaketRecipe newRecipe = new BruaketRecipe(name,barrel, result, talisman, time, recipe);
        addRecipe(newRecipe);
        return newRecipe;
    }

    @Override
    public IRecipe makeAndReplaceRecipe (String name, String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe) throws IndexOutOfBoundsException {
        ResourceLocation nameResolved = new ResourceLocation(Bruaket.MODID, name);
        if (!RECIPE_LIST.containsKey(nameResolved)) {
            throw new IndexOutOfBoundsException("Key '" + name + "' is not contained in the recipe list; use `makeAndAddRecipe` instead or check your spelling.");
        }
        BruaketRecipe newRecipe = new BruaketRecipe(name, barrel, result, talisman, time, recipe);
        addRecipe(newRecipe);
        return newRecipe;
    }

    @Override
    public void addRecipe (IRecipe recipe) {
        IMMUTABLE_COPY = null;
        RECIPE_LIST.put(recipe.getName(), recipe);

        String splitter = recipe.getTailsman() + "&" + recipe.getBarrel();

        if(!DIVIDED_LIST.containsKey(splitter)){
            DIVIDED_LIST.put(splitter, new LinkedList<>());
        }
        DIVIDED_LIST.get(splitter).add(recipe);
    }

    @Override
    @Nullable
    public IRecipe getRecipe (ResourceLocation name) {
        return RECIPE_LIST.get(name);
    }

    @Override
    public void removeRecipe (IRecipe recipe) {
        IMMUTABLE_COPY = null;
        RECIPE_LIST.remove(recipe.getName());
    }

    @Override
    public void removeRecipe (ResourceLocation name) {
        IMMUTABLE_COPY = null;
        RECIPE_LIST.remove(name);
    }

    @Override
    public ItemStack getOutputByIndex (int index) {
        if (index < 0 || index >= RECIPE_LIST.size()) {
            return ItemStack.EMPTY;
        }

        return getRecipeList().get(index).getRecipeOutput().copy();
    }

    @Override
    @Nullable
    public IRecipe getRecipeByIndex (int index) {
        if (index < 0 || index >= RECIPE_LIST.size()) {
            return null;
        }

        return getRecipeList().get(index);
    }

    @Override
    public int indexOf (IRecipe recipe) {
        if (recipe == null || !RECIPE_LIST.containsKey(recipe.getName())) {
            return -1;
        }

        return getRecipeList().indexOf(recipe);
    }

    @Override
    public int size () {
        return RECIPE_LIST.size();
    }

}
