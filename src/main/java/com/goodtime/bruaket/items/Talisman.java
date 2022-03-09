package com.goodtime.bruaket.items;

import com.goodtime.bruaket.core.Bruaket;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Talisman extends Item {

    private final String MODID = Bruaket.MODID;

    public Talisman(String registerName){
        this.setRegistryName(MODID, registerName);
        this.setUnlocalizedName(MODID+"."+registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(this);
        modelRegister(this);
    }

    public static void modelRegister(Item talisman){
        ModelResourceLocation model = new ModelResourceLocation(talisman.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(talisman, 0, model);
    }

    public static Item getItem(String registerName){
        Item item = new Item();
        item.setRegistryName(Bruaket.MODID, registerName);
        item.setUnlocalizedName(Bruaket.MODID+"."+registerName);
        ForgeRegistries.ITEMS.register(item);
        modelRegister(item);

        return item;
    }


}
