package com.goodtime.bruaket.recipe;

import com.goodtime.bruaket.entity.barrel.IBarrel;
import com.goodtime.bruaket.items.Talisman;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;

public class RecipeList  {

    private final HashMap<RecipeIngredients, IBruaketRecipe> recipesMap = new HashMap<>();
    private final HashMap<RecipeIngredients, FuzzyBruaketRecipe> fuzzyRecipesMap = new HashMap<>();
    public void addRecipe (IBruaketRecipe recipe) {
        recipesMap.put(recipe.getIngredients(), recipe);
    }
    @Nullable
    public IBruaketRecipe matches(RecipeIngredients ingredients) {
        return recipesMap.get(ingredients);
    }

    /**
     * Match a recipe using barrel's inventory.
     * @param barrelInventory item stacks of the barrel inventory. The array length must be 10,
     *                        and the first item must be the item stack of talisman.
     *                        Elements of this array can be null.
     * @return a @{@link IBruaketRecipe} if there is a match, otherwise null
     */
    public IBruaketRecipe matches(Talisman talisman, NonNullList<ItemStack> barrelInventory) {
        if (barrelInventory.size() != IBarrel.MAX_SIZE) {
            throw new IllegalArgumentException("Length of the item stacks must be " + IBarrel.MAX_SIZE);
        }

        if (talisman == null){
            return null;
        }

        HashSet<IIngredient> ingredients = new HashSet<>();
        for (int i = 1; i < barrelInventory.size(); i++) {
            ItemStack itemStack = barrelInventory.get(i);
            if(!itemStack.isEmpty()){
                ingredients.add(CraftTweakerMC.getIIngredient(itemStack));
            }
        }

        IBruaketRecipe recipe = matches(new RecipeIngredients(talisman.getRegistryName(), ingredients));

        if (recipe != null && recipe.matches(talisman.getRegistryName(), barrelInventory)) {
            return recipe;
        }

        // TODO: ore dict - fuzzy match
       // OreDictionary.getOreIDs()
        return null;
    }

   /* public static RecipeIngredients getFuzzyRecipeIngredients(RecipeIngredients recipeIngredients) {
        for (IIngredient ing : recipeIngredients.getIngredients()) {

        }
        RecipeIngredients ret = new RecipeIngredients(recipeIngredients.getTalisman());
    }*/
}
