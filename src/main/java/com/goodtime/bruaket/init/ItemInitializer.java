package com.goodtime.bruaket.init;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemInitializer {
    //Block
    public static Block wooden_barrel;

    //Item
    public static Item logo;
    public static Item bucket;

    //Talisman
    public static Talisman stone_talisman;
    public static Talisman fire_talisman;
    public static Talisman wood_talisman;
    public static Talisman iron_talisman;
    public static Talisman water_talisman;
    public static Talisman ultra_flamma_talisman;
    public static Talisman maxima_flamma_talisman;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event){

        //Item
        logo = Talisman.getItem("logo");
        bucket = Talisman.getItem("bucket").setCreativeTab(Bruaket.CREATIVE_TAB);

        //Barrel
        wooden_barrel = new Barrel("wooden_barrel");

        //Talisman
        stone_talisman = new Talisman("stone_talisman");
        fire_talisman = new Talisman("fire_talisman");
        wood_talisman = new Talisman("wood_talisman");
        iron_talisman = new Talisman("iron_talisman");
        water_talisman = new Talisman("water_talisman");
        ultra_flamma_talisman = new Talisman("ultra_flamma_talisman");
        maxima_flamma_talisman = new Talisman("maxima_flamma_talisman");
    }

}
