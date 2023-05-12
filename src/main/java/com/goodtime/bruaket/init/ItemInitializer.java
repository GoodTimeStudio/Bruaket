package com.goodtime.bruaket.init;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.blocks.NetherBarrel;
import com.goodtime.bruaket.blocks.OrdinaryBarrel;
import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.items.FlammaTalisman;
import com.goodtime.bruaket.items.Talisman;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemInitializer {
    //Block
    public static Barrel wooden_barrel;
    public static Barrel iron_barrel;
    public static Barrel tinker_barrel;

    public static Barrel nether_barrel;

    //Item
    public static Item logo;

    //public static Item bucket;

    public static Item bucketSeed;

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
        registerItems();
    }

    public static void registerItems() {
        //Item
        logo = Talisman.getItem("logo");
        //bucket = Talisman.getItem("bucket").setCreativeTab(Bruaket.CREATIVE_TAB);

        bucketSeed = Talisman.getItem("bucket_seed").setCreativeTab(Bruaket.CREATIVE_TAB);

        //Barrel
        wooden_barrel = new OrdinaryBarrel("wooden_barrel", 3.0F);
        iron_barrel = new OrdinaryBarrel("iron_barrel", 6.0F);
        tinker_barrel = new OrdinaryBarrel("tinker_barrel", 6.0F);

        nether_barrel = new NetherBarrel("nether_barrel", 10.0F);

        //Talisman
        stone_talisman = new Talisman("stone_talisman");
        wood_talisman = new Talisman("wood_talisman");
        iron_talisman = new Talisman("iron_talisman");
        water_talisman = new Talisman("water_talisman");

        fire_talisman = new FlammaTalisman("fire_talisman",1,1, 200,1000);
        ultra_flamma_talisman = new FlammaTalisman("ultra_flamma_talisman",3,8, 180, 10800);
        maxima_flamma_talisman = new FlammaTalisman("maxima_flamma_talisman",9,32, 130, 98800);
    }

}
