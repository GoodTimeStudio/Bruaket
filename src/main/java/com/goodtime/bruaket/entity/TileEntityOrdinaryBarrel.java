package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.entity.bruaket.IBarrelTile;
import com.goodtime.bruaket.entity.bruaket.BarrelUtil;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import com.goodtime.bruaket.util.ItemUtils;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileEntityOrdinaryBarrel extends TileEntityLockableLoot implements IBarrelTile, ITickable {

    private Talisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_SIZE, ItemStack.EMPTY);

    private int craftCooldown = -1;

    private long tickedGameTime;

    private ItemStack outputResult;

    public boolean matchingRequired = true;

    public TileEntityOrdinaryBarrel() {
    }

    public TileEntityOrdinaryBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
    }

    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
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

        this.craftCooldown = compound.getInteger("CraftCooldown");
    }

    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        compound.setInteger("CraftCooldown", this.craftCooldown);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        compound.setString("Barrel", barrel.toString());

        if(this.hasTalisman()){
            NBTTagCompound talismanTag = new NBTTagCompound();
            ItemStack talismanStack = new ItemStack(this.getTalisman());
            talismanStack.writeToNBT(talismanTag);
            compound.setTag("Talisman", talismanTag);
        }

        return compound;
    }

    @Override
    public void markDirty(){
        super.markDirty();
        matchingRequired = true;
    }


    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.craftCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();

            if (this.isIdle()) {
                if(this.mayOutput()){
                    drop(outputResult,1,false);
                    outputResult = null;
                }
                this.setCraftCooldown(0);
            }
            this.updateBarrel();
        }
    }

    protected void updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isFull()) {
                matchingRequired = BarrelUtil.pullItems(this) || matchingRequired;
            }
            if (matchingRequired && this.isIdle() && !this.isEmpty()) {
                if(this.hasTalisman()){
                    IBruaketRecipe recipe = RecipeMatcher.OrdinaryRecipeMatch(barrel, talisman.getRegistryName(), inventory);
                    if(recipe != null){
                        matchingRequired = true;
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

    private void consumeIngredients(RecipeIngredients ingredients){
        for (IIngredient ingredient : ingredients.getIngredients()) {
            ItemStack itemStack = CraftTweakerMC.getItemStack(ingredient);
            for (ItemStack itemInBarrel : this.inventory) {
                if (itemInBarrel.isEmpty()) {
                    continue;
                }
                if (ItemUtils.areStacksEqualIgnoreSize(itemInBarrel, itemStack)) {
                    itemInBarrel.setCount(itemInBarrel.getCount() - itemStack.getCount());
                    break;
                }
            }
        }
    }

    @Override
    public void setInventorySlotContents(int index, @NotNull ItemStack stack) {
        this.getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public void setTalisman(Talisman talisman){
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

    @Override
    public void setCraftCooldown(int ticks) {
        this.craftCooldown = ticks;

    }

    @Override
    public boolean mayOutput() {
        return this.outputResult != null;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean isIdle() {
        return this.craftCooldown <= 0;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    public int getBlockMetadata() {
        return super.getBlockMetadata();
    }


    public @NotNull World getWorld(){
        return this.world;
    }

    public double getXPos() {
        return (double)this.pos.getX() + 0.5D;
    }


    public double getYPos() {
        return (double)this.pos.getY() + 0.5D;
    }


    public double getZPos() {
        return (double)this.pos.getZ() + 0.5D;
    }


    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
