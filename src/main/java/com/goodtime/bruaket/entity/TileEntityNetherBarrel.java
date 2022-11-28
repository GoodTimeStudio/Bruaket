package com.goodtime.bruaket.entity;

import com.goodtime.bruaket.entity.bruaket.BarrelTileEntity;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class TileEntityNetherBarrel extends BarrelTileEntity {
    private Talisman talisman;

    private ResourceLocation barrel;

    private NonNullList<ItemStack> inventory;

    private long tickedGameTime;

    private ItemStack outputResult;

    public boolean matchingRequired = true;

    private int craftCooldown = -1;

    int energy;

    @Override
    public void update() {

    }
    @Override
    public boolean mayOutput() {
        return false;
    }

    @Override
    public long getTickedGameTime() {
        return 0;
    }

    @Override
    public void setCraftCooldown(int ticks) {

    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean hasTalisman() {
        return false;
    }

    @Override
    public void setTalisman(Talisman talisman) {

    }

    @Override
    public Talisman getTalisman() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return 0;
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
