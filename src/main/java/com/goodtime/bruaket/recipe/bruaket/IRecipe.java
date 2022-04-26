package com.goodtime.bruaket.recipe.bruaket;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public interface IRecipe{

    ItemStack getRecipeOutput ();

    String getTailsman();

    List<IngredientStack> getIngredients ();

    /**
     * @return Returns the index within the main Bruaket Recipe List that this recipe is
     */
    int getIndex();

    long getTime();

    String getBarrel();

    int getIngredientsSize();

    /**
     * @return Returns the ResourceLocation associated with the recipe.
     */
    ResourceLocation getName ();

    /**
     * This is only called on the server side and the resulting output
     * replaces the output in what is given to the player.
     *
     * @param player The player currently crafting.
     * @param output The raw, base item.
     * @return The item to be given to the player instead.
     */
    ItemStack onCrafted (EntityPlayer player, ItemStack output);
    /**
     * Returns whether or not the player currently accessing the given tile entity
     * (Gem Cutter's Table specifically) is able to craft this recipe. Should generally
     * be left for default.
     *
     * @param player        The player crafting.
     * @param craftingTable The tile entity of the GCT.
     * @return boolean true if the recipe is craftable
     */
    boolean craftable (EntityPlayer player, TileEntity craftingTable);
    /**
     * Provides an access point for additional GCTCondition predicates
     * to be stored in conditional GCT recipes. By default, this does
     * nothing on a standard recipe.
     *
     * @param predicate An instance of GCTCondition
     * @return the current recipe (for chaining purposes)
     */
    default IRecipe addCondition (Condition predicate) {
        return this;
    }

    boolean contains(ItemStack itemStack);

    /**
     * This is used as a placeholder for a boolean-returning function that is called
     * before an item can be considered craftable, even if the recipe matches the
     * combined inventory.
     * <p>
     * *Note that the default GCTRecipe implementation does not currently support
     * conditions*.
     */
    @FunctionalInterface
    interface Condition {
        /**
         * @param player The player interacting with the GCT.
         * @param tile   The Gem Cutter's Table tile entity.
         * @return boolean true if the player can craft this recipe
         */
        boolean test (EntityPlayer player, TileEntity tile);
    }



}
