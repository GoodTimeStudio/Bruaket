package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.entity.bruaket.OrdinaryBarrelTile;
import com.goodtime.bruaket.entity.utils.BarrelUtil;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TileEntityOrdinaryBarrel extends OrdinaryBarrelTile {

    private Talisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_SIZE, ItemStack.EMPTY);

    private int craftCooldown;

    private long tickedGameTime;

    private ItemStack outputResult;

    public boolean matchingRequired = true;

    public TileEntityOrdinaryBarrel() {
    }

    public TileEntityOrdinaryBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
    }

    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.getItems());
        }

        compound.setInteger("CraftCooldown", this.getCraftCooldown());

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        compound.setString("Barrel", this.barrel.toString());

        if(this.hasTalisman()){
            NBTTagCompound talismanTag = new NBTTagCompound();
            ItemStack talismanStack = new ItemStack(this.getTalisman());
            talismanStack.writeToNBT(talismanTag);
            compound.setTag("Talisman", talismanTag);
        }

        return compound;
    }

    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.getItems());
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        if(compound.hasKey("Barrel")){
            this.barrel = new ResourceLocation(compound.getString("Barrel"));
        }

        if(compound.hasKey("Talisman")){
            NBTTagCompound talismanTag = compound.getCompoundTag("Talisman");
            this.setTalisman((Talisman) new ItemStack(talismanTag).getItem());
        }

        this.setCraftCooldown(compound.getInteger("CraftCooldown"));
    }

    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            processTick();
            this.tickedGameTime = this.world.getTotalWorldTime();
            if (this.isIdle()) {
                if(this.mayOutput()){
                    drop(outputResult,1,false);
                    outputResult = null;
                }
            }
            this.updateBarrel();
        }
    }

    protected void updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isFull()) {
                matchingRequired = this.pullItems() || matchingRequired;
            }
            if (canStart()) {
                if(this.hasTalisman()){
                    IBruaketRecipe recipe = RecipeMatcher.OrdinaryRecipeMatch(barrel, talisman.getRegistryName(), inventory);
                    if(recipe != null){
                        this.outputResult = CraftTweakerMC.getItemStack(recipe.getRecipeOutput());
                        this.setCraftCooldown(recipe.getTime());
                        consumeIngredients(recipe.getIngredients());
                        markDirty();
                    }else{
                        matchingRequired = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean canStart() {
        return matchingRequired && this.isIdle() && !this.isEmpty();
    }

    public void consumeIngredients(RecipeIngredients ingredients){
        for (IIngredient ingredient : ingredients.getIngredients()) {
            ItemStack itemStack = CraftTweakerMC.getItemStack(ingredient);
            for (ItemStack itemInBarrel : this.inventory) {
                if (itemInBarrel.isEmpty()) continue;

                if (BarrelUtil.areStacksEqualIgnoreSize(itemInBarrel, itemStack)) {
                    itemInBarrel.setCount(itemInBarrel.getCount() - itemStack.getCount());
                    break;
                }
            }
        }
    }

    @Override
    public void setTalisman(Talisman talisman) {
        this.talisman = talisman;
    }


    @Override
    public Talisman getTalisman() {
        return hasTalisman() ? this.talisman : null;
    }


    public boolean hasTalisman() {
        return talisman != null;
    }


    @Override
    public long getTickedGameTime() {
        return this.tickedGameTime;
    }

    public void processTick() {
        if(this.craftCooldown >0){
            --craftCooldown;
        }
    }

    @Override
    public void setCraftCooldown(int ticks) {
        this.craftCooldown = ticks;
    }

    @Override
    public boolean mayOutput() {
        return this.outputResult != null;
    }

    @Override
    public boolean matchingRequired() {
        return this.matchingRequired;
    }

    @Override
    public void setMatchingRequired(boolean required) {
        this.matchingRequired = required;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int getCraftCooldown() {
        return this.craftCooldown;
    }

    @Override
    public boolean isIdle() {
        return this.craftCooldown <= 0;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }


}
