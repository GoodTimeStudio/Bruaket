package com.goodtime.bruaket.items;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.init.ModelMapper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemBruaket extends Item {

    private final String MODID = Bruaket.MODID;

    public ItemBruaket(String registerName){
        this.setRegistryName(MODID, registerName);
        this.setUnlocalizedName(MODID+"."+registerName);
        ModelMapper.registerItemRender(this);
        ForgeRegistries.ITEMS.register(this);
    }


}
