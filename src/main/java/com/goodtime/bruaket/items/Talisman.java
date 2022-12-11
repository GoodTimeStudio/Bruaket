package com.goodtime.bruaket.items;

import com.goodtime.bruaket.core.Bruaket;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

import static com.goodtime.bruaket.core.Bruaket.MODID;

public class Talisman extends Item {

    public Talisman(String registerName){
        this.setRegistryName(MODID, registerName);
        this.setUnlocalizedName(MODID+"."+registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        ForgeRegistries.ITEMS.register(this);
        modelRegister(this);
    }

    public static void modelRegister(Item talisman){
        ModelResourceLocation model = new ModelResourceLocation(Objects.requireNonNull(talisman.getRegistryName()), "inventory");
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
