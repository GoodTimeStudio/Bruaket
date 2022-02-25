package com.goodtime.bruaket.init;

import com.goodtime.bruaket.blocks.Bucket;
import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.items.ItemBruaket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Initializer {
    //Block
    public static Block simple_bucket;

    //Item
    public static Item logo;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event){
        logo = new ItemBruaket("logo");
        simple_bucket = new Bucket("simple_bucket");
    }

}
