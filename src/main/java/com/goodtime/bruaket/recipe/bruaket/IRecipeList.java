package com.goodtime.bruaket.recipe.bruaket;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface IRecipeList {

    /**
     * @return The current ResourceLocation->IGCTRecipe map of recipes.
     */
    Map<ResourceLocation, IRecipe> getRecipes ();

    /**
     * Attempts to find a recipe which creates the output specified.
     *
     * @param output The output of the recipe.
     * @return The IGCTRecipe or null if none was found.
     */
    @Nullable
    IRecipe getRecipeByOutput (ItemStack output);

    /**
     * @return Returns an ordered list of recipes according to their
     * addition in the RecipeLibrary.
     * <p>
     * By default this list is an Immutable copy.
     */
    List<IRecipe> getRecipeList ();

    /**
     * Creates an IGCTRecipe instance using the default GCTRecipe with
     * the name as the path of the ResourceLocation. The result is specified
     * and then the ingredients as varargs.
     *
     * @param name   The string name used to refer to this recipe. Used in the
     *               ResourceLocation mapping.
     * @param result The itemstack created by this recipe.
     * @param recipe The ingredients used to create this recipe. By default this
     *               can consist of any combination of ItemStack (with quantity),
     *               Item (default quantity of 1), Ingredient instance (default quantity
     *               of 1), String specifying an ore dictionary value (default quantity
     *               of 1), Block (default quantity of 1), or IngredientStack.
     *               Any other type will print a warning message to chat.
     * @return An instance of IGCTRecipe, a GCTRecipe by default.
     */
    IRecipe makeAndAddRecipe (String name, String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe);

    /**
     * As above, but instead of adding a new recipe will attempt to replace the recipe
     * using the current name. This ensures that the continuity of the recipe list remains
     * the same when modifying pre-existing recipes.
     * <p>
     * Will throw an IndexOutOfBoundsException if the name is not contained within the recipe
     * map.
     *
     * @param name   The name to replace.
     * @param result As per makeAndAddRecipe.
     * @param recipe As per makeAndAddRecipe.
     * @return As per makeAndAddRecipe.
     */
    IRecipe makeAndReplaceRecipe (String name, String barrel, @Nonnull ItemStack result, String talisman, int time, Object... recipe) throws IndexOutOfBoundsException;


    void addRecipe (IRecipe recipe);

    /**
     * Attempts to find the specified recipe by ResourceLocation name. Can
     * be null if not found.
     *
     * @param name The ResourceLocation mapping for the recipe being sought.
     * @return The relevant IGCTRecipe or null if not found.
     */
    @Nullable
    IRecipe getRecipe (ResourceLocation name);

    /**
     * Attempts to remove a recipe using the recipe instance.
     * <p>
     * No indication of success or failure.
     *
     * @param recipe The recipe to be removed.
     */
    void removeRecipe (IRecipe recipe);

    /**
     * As per removeRecipe but via the ResourceLocation mapping instead.
     *
     * @param name The ResourceLocation of the recipe to be removed.
     */
    void removeRecipe (ResourceLocation name);

    /**
     * Attempts first to find the recipe at the specified index
     * and then return its recipe output.
     *
     * @param index The index into the ImmutableList copy.
     * @return The ItemStack output or ItemStack.EMPTY if not found
     */
    ItemStack getOutputByIndex (int index);

    /**
     * Attempts to fetch a recipe via its index in the immutable list.
     *
     * @param index The index into the ImmutableList copy.
     * @return The index or null if not found.
     */
    @Nullable
    IRecipe getRecipeByIndex (int index);

    /**
     * @param recipe The recipe being sought.
     * @return The index of the ImmutableList of recipes or -1 if not found.
     */
    int indexOf (IRecipe recipe);

    /**
     * @return The number of registered recipes.
     */
    int size ();



}
