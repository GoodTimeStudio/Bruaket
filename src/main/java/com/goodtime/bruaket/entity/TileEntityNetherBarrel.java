package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.energy.BarrelEnergyStorage;
import com.goodtime.bruaket.entity.bruaket.PoweredBarrel;
import com.goodtime.bruaket.entity.utils.BarrelUtil;
import com.goodtime.bruaket.items.FlammaTalisman;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TileEntityNetherBarrel extends PoweredBarrel {

    private FlammaTalisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory;

    private int maxSmeltingCount;

    private long tickedGameTime;

    private final ArrayList<ItemStack> outputResults = new ArrayList<>();

    public boolean matchingRequired = true;

    private int craftCooldown;

    private int waitingTime;

    public TileEntityNetherBarrel() {}

    public TileEntityNetherBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
    }

    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (this.hasTalisman() && !this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.getItems());
        }

        compound.setInteger("CraftCooldown", this.getCraftCooldown());

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        compound.setString("Barrel", this.getBarrel());

        if(this.hasTalisman()){
            NBTTagCompound talismanTag = new NBTTagCompound();
            ItemStack talismanStack = new ItemStack(this.talisman);
            talismanStack.writeToNBT(talismanTag);
            compound.setTag("FTalisman", talismanTag);
        }

        return compound;
    }

    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("FTalisman")){
            NBTTagCompound talismanTag = compound.getCompoundTag("FTalisman");
            FlammaTalisman fTalisman = (FlammaTalisman) new ItemStack(talismanTag).getItem();
            this.setTalisman(fTalisman);
            this.inventory = NonNullList.withSize(fTalisman.getSmeltingSlot(), ItemStack.EMPTY);
        }

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.getItems());
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        if(compound.hasKey("Barrel")){
            this.setBarrel(new ResourceLocation(compound.getString("Barrel")));
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
                    for (int i = 0; i < this.outputResults.size(); i++) {
                        ItemStack outputResult = outputResults.get(i);
                        drop(outputResult,outputResult.getCount(),false);
                        outputResults.remove(i);
                        i=i-1;
                    }
                }
            }
            this.updateBarrel();
        }
    }

    protected void updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if(this.getItems() == null ){
                this.pullItems();
            }else if (!this.isFull()) {
                matchingRequired = processPull() || matchingRequired;
            }

            if(this.hasTalisman()){
                if (canStart()) {
                    ArrayList<IBruaketRecipe> matchedRecipes = RecipeMatcher.SmeltingRecipeMatch(this, barrel, inventory);
                    if(!matchedRecipes.isEmpty()){
                        this.setCraftCooldown(this.talisman.getSmeltTime());
                        consumeIngredients(matchedRecipes);
                        markDirty();
                    }else{
                        matchingRequired = false;
                    }
                }
            }
        }
    }

    public boolean canStart(){
        return waitingTime<=0 && energyStorage.hasEnergy() && matchingRequired && this.isIdle() && !this.isEmpty();
    }


    private boolean processPull(){
        if(waitingTime > 0){
            --this.waitingTime;
            return this.pullItems();
        }else if(this.isEmpty()){
            boolean pullResult = this.pullItems();
            this.waitingTime = pullResult ? 30 : 0;
            return pullResult;
        }else {
            return this.pullItems();
        }
    }

    @Override
    public void processTick() {
        if(this.hasTalisman() && this.craftCooldown > 0 && this.waitingTime <= 0){
            --craftCooldown;
            this.energyStorage.modifyEnergyStored(-this.talisman.getSmeltWaste());
        }
    }

    public void consumeIngredients(ArrayList<IBruaketRecipe> recipes) {
        int smeltCount = 0;

        int maxSmeltingCount = this.talisman.getMaxSmeltingCount();
        for (IBruaketRecipe recipe : recipes) {

            if(smeltCount == maxSmeltingCount) break;

            IIngredient ingredient = recipe.getIngredients().getIngredients().iterator().next();
            ItemStack smeltingIngredient = CraftTweakerMC.getItemStack(ingredient);
            ItemStack smeltingResult = CraftTweakerMC.getItemStack(recipe.getRecipeOutput());

            for (int i = 0; i < this.inventory.size(); i++) {
                ItemStack itemInBarrel = inventory.get(i);
                if (itemInBarrel.isEmpty()) continue;
                if (BarrelUtil.areStacksEqualIgnoreSize(itemInBarrel, smeltingIngredient)) {
                    int count = consume(i, maxSmeltingCount - smeltCount);
                    smeltCount += count;
                    smeltingResult.setCount(count);
                    outputResults.add(smeltingResult);
                    break;
                }
            }
        }
    }

    public int consume(int index, int remainingConsumption){
        ItemStack itemInBarrel = inventory.get(index);
        if(itemInBarrel.getCount() > remainingConsumption){
            itemInBarrel.setCount(itemInBarrel.getCount() - remainingConsumption);
            return remainingConsumption;
        }else {
            return ItemStackHelper.getAndSplit(this.getItems(), index, remainingConsumption).getCount();
        }
    }


    @Override
    public boolean mayOutput() {
        return !this.outputResults.isEmpty();
    }

    @Override
    public long getTickedGameTime() {
        return this.tickedGameTime;
    }

    @Override
    public boolean isIdle() {
        return this.craftCooldown <= 0;
    }

    @Override
    public boolean hasTalisman() {
        return talisman != null;
    }

    @Override
    public ItemStack putTalisman(Talisman talisman) {

        if(!(talisman instanceof FlammaTalisman)){
            return new ItemStack(talisman);
        }

        if (!this.hasTalisman()) {
            this.setTalisman(talisman);
            this.markDirty();
            return ItemStack.EMPTY;
        } else {
            return new ItemStack(talisman);
        }
    }

    private void clearData(){
        this.talisman = null;
        this.inventory = null;
        this.energyStorage = new BarrelEnergyStorage(0,0);
        this.craftCooldown = 0;
        this.outputResults.clear();
    }

    @Override
    public void setTalisman(Talisman talisman) {
        if(talisman == null){
            clearData();
        }else {
            FlammaTalisman fTalisman = (FlammaTalisman) talisman;
            this.talisman = fTalisman;
            this.inventory = NonNullList.withSize(fTalisman.getSmeltingSlot(), ItemStack.EMPTY);
            this.energyStorage = new BarrelEnergyStorage(fTalisman.getSmeltingConsumption() * 200, fTalisman.getSmeltWaste() * 5);
            this.maxSmeltingCount = fTalisman.getMaxSmeltingCount();
        }
    }

    @Override
    public void dropTalisman() {
        if(!this.isEmpty()){
            this.dropAllItems();
        }
        super.dropTalisman();
    }

    @Override
    public boolean putDropInInventoryAllSlots(IInventory source, EntityItem entity) {
        if(inventory == null){
            if(entity.getItem().getItem() instanceof Talisman){
                return super.putDropInInventoryAllSlots(source,entity);
            }else {
                return false;
            }
        }else {
            return super.putDropInInventoryAllSlots(source,entity);
        }
    }

    @Override
    public int getCraftCooldown() {
        return this.craftCooldown;
    }

    @Override
    public void setCraftCooldown(int ticks) {
        this.craftCooldown = ticks;
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
    public void setItems(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }


    @Override
    public String getBarrel() {
        return this.barrel.toString();
    }

    @Override
    public void setBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
    }

    @Override
    public void dropAllItems() {
        if(this.inventory != null){
            super.dropAllItems();
        }

    }

    @Override
    public void dropLastItem() {
        if(this.inventory != null) {
            super.dropLastItem();
        }
    }

    @Override
    public Talisman getTalisman() {
        return this.talisman;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }



    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    public int getBlockMetadata() {
        return super.getBlockMetadata();
    }

}
