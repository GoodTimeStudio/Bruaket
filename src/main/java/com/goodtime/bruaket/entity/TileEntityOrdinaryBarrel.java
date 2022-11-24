package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.entity.bruaket.BarrelTileEntity;
import com.goodtime.bruaket.entity.bruaket.BarrelUtil;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.bruaket.IBruaketRecipe;
import com.goodtime.bruaket.recipe.RecipeIngredients;
import com.goodtime.bruaket.recipe.RecipeMatcher;
import com.goodtime.bruaket.util.ItemUtils;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityOrdinaryBarrel extends BarrelTileEntity {

    private Talisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(MAX_SIZE, ItemStack.EMPTY);

    private int transferCooldown = -1;

    private long tickedGameTime;

    private ItemStack outputResult;

    private boolean isWorking;

    public TileEntityOrdinaryBarrel() {
    }

    public TileEntityOrdinaryBarrel(ResourceLocation barrel) {
        this.barrel = barrel;
    }

    public void readFromNBT(NBTTagCompound compound) {
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
            this.setTalisMan((Talisman) new ItemStack(talismanTag).getItem());
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        compound.setString("Barrel", barrel.toString());

        if(this.hasTalisMan()){
            NBTTagCompound talismanTag = new NBTTagCompound();
            ItemStack talismanStack = new ItemStack(this.getTalisMan());
            talismanStack.writeToNBT(talismanTag);
            compound.setTag("Talisman", talismanTag);
        }

        return compound;
    }

    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();

            if (!this.isOnTransferCooldown()) {
                if(outputResult != null){
                    drop(outputResult,1,false);
                    outputResult = null;
                    this.isWorking = false;
                }
                this.setTransferCooldown(0);
            }
            this.updateBarrel();
        }
    }

    //一定tick之后执行的操作
    protected boolean updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isFull()) {
                BarrelUtil.pullItems(this);
            }
            if (!isWorking && !this.isEmpty()) {
                if(this.hasTalisMan()){
                    IBruaketRecipe recipe = RecipeMatcher.match(barrel, talisman.getRegistryName(), inventory);
                    if(recipe != null){
                        this.outputResult = CraftTweakerMC.getItemStack(recipe.getRecipeOutput());
                        this.setTransferCooldown(recipe.getTime());
                        consumeIngredients(recipe.getIngredients());
                        this.isWorking = true;
                    }
                }
                this.markDirty();
                return true;
            }

        }
        return false;
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

    //传输指定物品到其他有储存功能的方块
    private boolean transferItemsOut(ItemStack output, int count, boolean needDecrSize) {
        IInventory iinventory = this.getInventoryForBarrelTransfer();

        if (iinventory == null) {
            return false;
        }

        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata()).getOpposite();

        if (BarrelUtil.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }

        System.out.println("现在丢出的物品是："+output.getItem().getRegistryName());


        if (!needDecrSize) {
            ItemStack itemStack = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, output, enumfacing);
            if (itemStack.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
        } else {
            ItemStack backups = output.copy();
            ItemStack itemStack = TileEntityHopper.putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(output, count), enumfacing);
            if (itemStack.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
            this.setInventorySlotContents(indexOf(output), backups);
        }
        return false;
    }


    //获取桶底部的方块的库存（inventory），如果没有库存就返回null
    private IInventory getInventoryForBarrelTransfer() {
        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata());
        return TileEntityHopper.getInventoryAtPosition(this.getWorld(), this.getXPos() + (double) enumfacing.getFrontOffsetX(), this.getYPos() + (double) enumfacing.getFrontOffsetY(), this.getZPos() + (double) enumfacing.getFrontOffsetZ());
    }

    @Override
    public void setTalisMan(Talisman talisman){
        this.talisman = talisman;
    }


    public Talisman getTalisMan() {
        return hasTalisMan() ? this.talisman : null;
    }


    public boolean hasTalisMan() {
        return talisman != null;
    }


    //如果底下的方块是空气就直接喷射，如果是储存方块就丢进去
    public boolean drop(ItemStack itemstack, int count, boolean needDecrSize) {

        World worldIn = this.world;
        double x = this.getXPos();
        double y = this.getYPos() - 1;
        double z = this.getZPos();

        System.out.println("当前抛出的物品是:"+itemstack.getItem().getRegistryName()+" *"+itemstack.getCount());

        if (BarrelUtil.bottomIsAir(this)) {
            if(!needDecrSize){
                BehaviorDefaultDispenseItem.doDispense(worldIn, itemstack, 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
            }else {
                BehaviorDefaultDispenseItem.doDispense(worldIn, decrStackSize(itemstack, count), 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
                markDirty();
            }
            return true;
        } else{
            return transferItemsOut(itemstack, count, needDecrSize);
        }
    }

    //全部合成栏是否为空
    @Override
    public double getXPos() {
        return (double)this.pos.getX() + 0.5D;
    }
    @Override
    public double getYPos() {
        return (double)this.pos.getY() + 0.5D;
    }
    @Override
    public double getZPos() {
        return (double)this.pos.getZ() + 0.5D;
    }

    //全部合成栏是否都满了

    @Override
    public boolean isFull() {
        for (ItemStack itemstack : this.inventory) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public long getTickedGameTime() {
        return this.tickedGameTime;
    }
    @Override
    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;

    }

    @Override
    public boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    private boolean isInventoryEmpty() {
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return this.isInventoryEmpty();
    }


    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    //减少格子内指定数量的指定物品
    public ItemStack decrStackSize(ItemStack itemStack, int count) {
        return ItemStackHelper.getAndSplit(this.getItems(), indexOf(itemStack), count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.fillWithLoot(null);
        this.getItems().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public int indexOf(ItemStack itemStack) {
        return this.getItems().indexOf(itemStack);
    }

    public boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
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
