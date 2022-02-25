package com.goodtime.bruaket.items;

import com.goodtime.bruaket.core.Bruaket;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemBruaket extends Item {

    private final String MODID = Bruaket.MODID;

    public ItemBruaket(String registerName){
        this.setRegistryName(MODID, registerName);
        this.setUnlocalizedName(MODID+"."+registerName);
        ForgeRegistries.ITEMS.register(this);
        modelRegister();
    }

    private void modelRegister(){
        ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(this, 0, model);
    }

}
