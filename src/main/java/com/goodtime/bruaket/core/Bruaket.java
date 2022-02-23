package com.goodtime.bruaket.core;

import com.goodtime.bruaket.init.Initializer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Bruaket.MODID, name = Bruaket.NAME, version = Bruaket.VERSION)
public enum Bruaket {

    INSTANCE;

    public static final String MODID = "bruaket";
    public static final String NAME = "Bruaket";
    public static final String VERSION = "1.0";
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs("Bruaket") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Initializer.logo);
        }
    };

    @Mod.InstanceFactory
    public static Bruaket getInstance() {
        return INSTANCE;
    }

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
    }

}
