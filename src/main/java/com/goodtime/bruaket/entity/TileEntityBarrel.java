package com.goodtime.bruaket.entity;


import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.Talisman;
import com.goodtime.bruaket.recipe.IngredientStack;
import com.goodtime.bruaket.recipe.RecipeList;
import com.goodtime.bruaket.recipe.bruaket.IRecipe;
import net.minecraft.block.Block;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TileEntityBarrel extends TileEntityHopper {

   private UUID owner = null;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);

    private long tickedGameTime;

    private LinkedList<IRecipe> recipeListFromSplitter;

    private final String barrel = getName();

    private LinkedList<IRecipe> currentRecipes = null;

    private StringBuffer history = new StringBuffer();

    private ItemStack result;

    //传输速度
    private int transferCooldown = -1;

    //读取NBT，确保物品在重启游戏后不会丢失且数量不变
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.inventory);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }

        this.transferCooldown = compound.getInteger("TransferCooldown");

        this.history = new StringBuffer(compound.getString("Recipe"));

        if(haveTailMan()){
            this.setSplitterRecipeList(this.getTailsMan());
        }

        this.currentRecipes = this.getLastHistory();

    }

    //存储NBT
    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.inventory);
        }

        compound.setInteger("TransferCooldown", this.transferCooldown);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        if (this.history.length() > 0) {
            compound.setString("Recipe", this.history.toString());
        }

        return compound;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    @Nonnull
    public String getName() {
        return "wooden_barrel";
    }

    public LinkedList<IRecipe> getRecipeListFromSplitter() {
        return recipeListFromSplitter;
    }

    public void setSplitterRecipeList(Talisman talisman) {
        this.recipeListFromSplitter = RecipeList.instance.getRecipeListByBarrelAndTalisMan(this.getBarrel(), Objects.requireNonNull(talisman.getRegistryName()).toString().split(":")[1]);
        this.markDirty();
    }

    public void setCurrentRecipe(LinkedList<IRecipe> currentRecipes) {
        this.currentRecipes = currentRecipes;
        this.markDirty();
    }

    public LinkedList<IRecipe> getCurrentRecipe() {
        return currentRecipes;
    }

    public void addHistory(LinkedList<IRecipe> recipes) {

        if(recipes == null){
            history.append("n").append("&").append("#");
        }else {
            for (IRecipe recipe : recipes) {
                history.append(recipe.getIndex()).append("&");
            }
            history.append("#");
            this.markDirty();
        }
    }

    public void clearHistory() {
        history = new StringBuffer();
        this.markDirty();
    }

    public void clearRecipe() {
        setCurrentRecipe(null);
        clearHistory();
        this.markDirty();
    }

    public void removeLastHistory() {
        String[] temp = history.toString().split("#");

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < temp.length - 1; i++) {
            sb.append(temp[i]).append("#");
        }

        history = sb;
        this.markDirty();

    }

    public LinkedList<IRecipe> getLastHistory() {

        if (this.history.length() > 0) {
            String[] temp = history.toString().split("#");

            String[] indexes = temp[temp.length - 1].split("&");

            return historyTOList(indexes);
        } else if (this.haveTailMan()) {
            return this.getRecipeListFromSplitter();
        }
        return null;
    }


    public LinkedList<IRecipe> getPreviousHistory() {
        if (history.length() > 0) {

            if (history.toString().split("#").length == 1) {
                clearHistory();
                addHistory(recipeListFromSplitter);
                return recipeListFromSplitter;
            }

            removeLastHistory();

            String[] temp = history.toString().split("#");

            String[] indexes = temp[temp.length - 1].split("&");

            return historyTOList(indexes);
        }
        return recipeListFromSplitter;
    }

    private static LinkedList<IRecipe> historyTOList(String[] history) {
        LinkedList<IRecipe> recipes = new LinkedList<>();
        for (String s : history) {
            if(s.equals("n")){
                return null;
            }
            int index = Integer.parseInt(s);
            recipes.add(RecipeList.instance.getRecipeByIndex(index));
        }
        return recipes;
    }

    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.tickedGameTime = this.world.getTotalWorldTime();

            if (!this.isOnTransferCooldown()) {

                if(result != null){
                    this.drop(result, 1, true);
                    result = null;
                }

                this.setTransferCooldown(0);
                this.updateBarrel();
            }
        }
    }

    //漏斗执行的需要时间的操作
    protected boolean updateBarrel() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isOnTransferCooldown() && Barrel.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;

                //如果漏斗没满，接收投掷物
                if (!this.isCraftFull()) {
                    flag = pullItems(this);
                }

                if (flag) {
                    if(!this.isCraftEmpty() && this.canOut()){
                        IRecipe recipe = currentRecipes.get(0);

                        result = recipe.getRecipeOutput();

                        this.initializationRecipe();

                        this.setTransferCooldown((int)recipe.getTime());
                    }
                    this.markDirty();
                    return true;
                }
            }

        }
        return false;
    }

    public boolean canOut(){
        if(currentRecipes != null && currentRecipes.size() == 1){
            IRecipe recipe = currentRecipes.get(0);

            LinkedList<ItemStack> matchedItemStacks = new LinkedList<>();

            for (int i = 1; i< inventory.size(); i++) {

                ItemStack itemStack = inventory.get(i);

                for (IngredientStack ingredient : recipe.getIngredients()) {
                    for (ItemStack stacksWithSize : ingredient.getMatchingStacksWithSizes()) {
                        if(ItemStack.areItemStacksEqual(itemStack, stacksWithSize)){
                            matchedItemStacks.add(itemStack.copy());
                            decrStackSize(itemStack, stacksWithSize.getCount());
                            break;
                        }
                    }

                }
            }

            if(matchedItemStacks.size() == recipe.getIngredientsSize()){
                return true;
            }else {
                for (ItemStack matchedItemStack : matchedItemStacks) {
                    int index = indexOf(matchedItemStack);
                    if(index != -1){
                        setInventorySlotContents(index, matchedItemStack);
                    }else {
                        putCraftInInventorySlots((IInventory) null, this, matchedItemStack, (EnumFacing) null);
                    }
                }
                return false;
            }
        }
        return false;
    }

    //塞入掉落物
    public static boolean pullItems(TileEntityBarrel barrel) {
       /* Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(hopper);
        if (ret != null) return ret;*/

        List<EntityItem> items = getCaptureItems(barrel.getWorld(), barrel.getXPos(), barrel.getYPos(), barrel.getZPos());
        if (!items.isEmpty()) {
            EntityItem entityitem = items.get(0);
            return putDropInInventoryAllSlots((IInventory) null, barrel, entityitem);
        }
        return false;
    }

    //将掉落物塞进桶里，如果是符咒就塞最后一个格子里
    public static boolean putDropInInventoryAllSlots(IInventory source, TileEntityBarrel destination, EntityItem entity) {
        boolean flag = false;

        if (entity == null) {
            return false;
        } else {
            ItemStack drop = entity.getItem().copy();

            ItemStack result;

            if (drop.getItem() instanceof Talisman) {
                result = putTalisman(destination, drop);
            } else {
                //不放符文不能放其他物品进去，如果放了符文合成表依然为空也无法放置物品
                if (destination.haveTailMan()) {
                    result = putCraftInInventorySlots(source, destination, drop, (EnumFacing) null);
                } else {
                    result = null;
                }

                if (result != null) {
                    matching(destination, drop);
                }
            }

            if (result != null) {
                if (result.isEmpty()) {
                    flag = true;
                    entity.setDead();
                } else {
                    entity.setItem(result);
                }
            }

            return flag;
        }
    }

    //匹配合成表，如果放入这个物品后就匹配不到合成表了，那就把这个物品丢出去
    private static void matching(TileEntityBarrel destination, ItemStack itemStack) {
        LinkedList<IRecipe> recipes = RecipeList.instance.getRecipeListByItem(itemStack, destination.getCurrentRecipe());
        if(recipes != null){
            destination.setCurrentRecipe(recipes);
        }
        destination.addHistory(destination.getCurrentRecipe());
    }


    private static ItemStack putTalisman(TileEntityBarrel destination, ItemStack talisman) {
        if (destination.getStackInSlot(0).isEmpty()) {
            destination.setInventorySlotContents(0, talisman);

            destination.setSplitterRecipeList(destination.getTailsMan());

            if (destination.getRecipeListFromSplitter() != null) {
                System.out.println("已匹配到：" + destination.getRecipeListFromSplitter().size() + "个初始合成：");
                destination.getRecipeListFromSplitter().forEach(recipe -> System.out.print(recipe.getName().toString()));
            } else {
                System.out.println("未匹配到合成");
            }

            //放入符文的时候将当前合成表集合初始化为以符文和当前桶筛选出的合成
            destination.initializationRecipe();


            talisman = ItemStack.EMPTY;

            destination.markDirty();
            return talisman;
        } else {
            return null;
        }
    }

    public static ItemStack putCraftInInventorySlots(IInventory source, TileEntityBarrel destination, ItemStack stack, @Nullable EnumFacing direction) {

        int i = destination.getSizeInventory();

        for (int j = 1; j < i && !stack.isEmpty(); ++j) {
            stack = insertCraft(source, destination, stack, j, direction);
        }

        return stack;
    }


    public static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
        if (!inventoryIn.isItemValidForSlot(index, stack)) {
            return false;
        } else {
            return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory) inventoryIn).canInsertItem(index, stack, side);
        }
    }

    public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else if (stack1.getMetadata() != stack2.getMetadata()) {
            return false;
        } else if (stack1.getCount() > stack1.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }

    public static ItemStack insertCraft(IInventory source, IInventory destination, ItemStack stack, int index, EnumFacing direction) {
        ItemStack itemstack = destination.getStackInSlot(index);
        boolean flag = false;
        boolean flag1 = destination.isEmpty();


        if (canInsertItemInSlot(destination, stack, index, direction)) {

            if (itemstack.isEmpty()) {
                destination.setInventorySlotContents(index, stack);
                stack = ItemStack.EMPTY;
                flag = true;
            } else if (canCombine(itemstack, stack)) {
                int i = stack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }

            if (flag) {
                if (flag1) {

                    if (!((TileEntityBarrel) destination).mayTransfer()) {
                        int k = 0;

                        if (source instanceof TileEntityHopper) {
                            TileEntityBarrel tileEntityBarrel2 = (TileEntityBarrel) source;

                            if (((TileEntityBarrel) destination).tickedGameTime >= tileEntityBarrel2.tickedGameTime) {
                                k = 1;
                            }
                        }

                        ((TileEntityBarrel) destination).setTransferCooldown(8 - k);
                    }
                }

                destination.markDirty();
            }

        }

        return stack;
    }


    public ItemStack decrStackSize(ItemStack itemStack, int count) {
        this.fillWithLoot((EntityPlayer) null);
        return ItemStackHelper.getAndSplit(this.getItems(), indexOf(itemStack), count);
    }

    //传输指定物品到其他有储存功能的方块
    private boolean transferItemsOut(ItemStack output, int count, boolean isResult) {
        IInventory iinventory = this.getInventoryForBarrelTransfer();

        if (iinventory == null) {
            return false;
        }

        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata()).getOpposite();

        if (this.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }

        System.out.println("现在丢出的物品是："+output.getItem().getRegistryName());

        if (isResult) {
            ItemStack itemStack = putStackInInventoryAllSlots(this, iinventory, output, enumfacing);
            if (itemStack.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
        } else {
            ItemStack itemStack = output.copy();
            ItemStack itemStack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(output, count), enumfacing);
            if (itemStack1.isEmpty()) {
                iinventory.markDirty();
                return true;
            }
            this.setInventorySlotContents(indexOf(output), itemStack);
        }
        return false;

    }

    public int tailsmanSlot() {
        return 0;
    }

    public Talisman getTailsMan() {
        return haveTailMan() ? (Talisman) this.inventory.get(tailsmanSlot()).getItem() : null;
    }


    //如果桶下方的方块是空气
    protected boolean bottomIsAir(IHopper barrel) {
        World worldIn = barrel.getWorld();
        int x = MathHelper.floor(barrel.getXPos());
        int y = MathHelper.floor(barrel.getYPos()) - 1;
        int z = MathHelper.floor(barrel.getZPos());

        Block atDown = worldIn.getBlockState(new BlockPos(x, y, z)).getBlock();

        return atDown.equals(Blocks.AIR);

    }

    //如果底下的方块是空气就直接喷射，如果是储存方块就丢进去
    protected boolean drop(ItemStack itemstack, int count, boolean isResult) {

        World worldIn = this.world;
        double x = this.getXPos();
        double y = this.getYPos() - 1;
        double z = this.getZPos();

        System.out.println("当前抛出的物品是:"+itemstack.getItem().getRegistryName());

        if (bottomIsAir(this)) {
            if(isResult){
                BehaviorDefaultDispenseItem.doDispense(worldIn, itemstack, 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
            }else {
                BehaviorDefaultDispenseItem.doDispense(worldIn, decrStackSize(itemstack, count), 3, EnumFacing.DOWN, new PositionImpl(x, y, z));
            }
            return true;
        } else{
            return transferItemsOut(itemstack, count, isResult);
        }
    }

    //获取最后一个放入的物品
    private ItemStack getLastItem() {
        ItemStack itemStack = null;
        for (int i = this.getSizeInventory() - 1; i >= 1; i--) {
            itemStack = this.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                break;
            }
        }

        return itemStack;
    }

    //抛出所有储存的物品(除了符文)
    public static void dropAllItems(TileEntityBarrel tileEntity) {
        if(!tileEntity.isEmpty()){
            if (tileEntity.isCraftEmpty()) {
                dropTalisman(tileEntity);
            } else {
                for (int i = 1; i < tileEntity.getSizeInventory(); i++) {
                    ItemStack itemStack = tileEntity.getStackInSlot(i);
                    if (!itemStack.isEmpty()) {
                        tileEntity.drop(itemStack, itemStack.getCount(), false);
                    }
                }
                tileEntity.initializationRecipe();
            }
        }

    }

    //抛出最后放入的物品
    public static void dropLastItem(TileEntityBarrel tileEntity) {
        if(!tileEntity.isEmpty()){
            if (tileEntity.isCraftEmpty()) {
                dropTalisman(tileEntity);
            } else {
                ItemStack itemStack = tileEntity.getLastItem();
                if (itemStack != null) {
                    tileEntity.drop(itemStack, itemStack.getCount(), false);
                    tileEntity.setCurrentRecipe(tileEntity.getPreviousHistory());
                }
            }
        }
    }

    public static void dropTalisman(TileEntityBarrel tileEntity) {
        if(!tileEntity.isEmpty()) {
            tileEntity.drop(tileEntity.getStackInSlot(0), 1, false);
            if (!tileEntity.isCraftEmpty()) {
                dropAllItems(tileEntity);
            }
            tileEntity.clearRecipe();
        }
    }

    public void initializationRecipe(){
        this.setCurrentRecipe(this.recipeListFromSplitter);
        this.clearHistory();
        this.addHistory(currentRecipes);
    }

    //获取桶底部的方块的库存（inventory），如果没有库存就返回null
    @Nullable
    private IInventory getInventoryForBarrelTransfer() {
        EnumFacing enumfacing = Barrel.getFacing(this.getBlockMetadata());
        return getInventoryAtPosition(this.getWorld(), this.getXPos() + (double) enumfacing.getFrontOffsetX(), this.getYPos() + (double) enumfacing.getFrontOffsetY(), this.getZPos() + (double) enumfacing.getFrontOffsetZ());
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint) {
                ItemStack itemStack = isidedinventory.getStackInSlot(k);

                if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxStackSize()) {
                    return false;
                }
            }
        } else {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean haveTailMan() {
        return !inventory.get(tailsmanSlot()).isEmpty();
    }


    public boolean isEmpty() {
        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //全部合成栏是否为空
    public boolean isCraftEmpty() {
        for (int i = 1; i < this.inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //全部合成栏是否都满了
    public boolean isCraftFull() {
        //遍历所有格子，如果格子是空的，或者物品的最大量不为可储存的最大量
        for (int i = 1; i < this.inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    public int indexOf(ItemStack itemStack) {
        return this.getItems().indexOf(itemStack);
    }

    public int getSizeInventory() {
        return this.inventory.size();
    }

    public String getBarrel() {
        return barrel;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public long getLastUpdateTime() {
        return tickedGameTime;
    } // Forge

}
