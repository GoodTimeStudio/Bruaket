package com.goodtime.bruaket.init;

import com.goodtime.bruaket.blocks.SimpleBucket;
import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.core.BruaketGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Initializer {

    private static final CreativeTabs BRUAKET_CREATIVE_TAB = new BruaketGroup();

    public static Item logo;

    public static Block simple_bucket = new SimpleBucket(Material.ROCK);

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
              simple_bucket
                       .setRegistryName(Bruaket.MODID, "simple_bucket")
                       .setCreativeTab(BRUAKET_CREATIVE_TAB)
                        .setUnlocalizedName("simple_bucket")
        );
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event){

        event.getRegistry().registerAll(
                new ItemBlock(simple_bucket).setRegistryName(Bruaket.MODID,"simple_bucket"),
                logo = new Item().setRegistryName(Bruaket.MODID,"logo")
        );
        registerRender();
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemRender(Item item){
        ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, model);

    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(){
       registerItemRender(logo);

    }

}
