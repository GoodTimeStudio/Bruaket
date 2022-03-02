package com.goodtime.bruaket.init;

import com.goodtime.bruaket.blocks.Barrel;
import com.goodtime.bruaket.items.ItemBruaket;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Initializer {
    //Block
    public static Block wooden_barrel;

    //Item
    public static Item logo;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event){
        logo = new ItemBruaket("logo");
        wooden_barrel = new Barrel("wooden_barrel");
    }

}
