package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.core.Bruaket;
import com.goodtime.bruaket.entity.TileEntityBucket;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Bucket extends BlockHopper {

    String name;


    public Bucket(String registerName) {
        super();
        name = registerName;
        this.setRegistryName(Bruaket.MODID, registerName);
        this.setCreativeTab(Bruaket.CREATIVE_TAB);
        this.setUnlocalizedName(Bruaket.MODID+"." + registerName);
        register();
    }

    private void register() {

        ForgeRegistries.BLOCKS.register(this);

        ForgeRegistries.ITEMS.register(new ItemBlock(this).setRegistryName(name));

        modelRegister();

        //GameRegistry.registerTileEntity(TileEntityEnderHopper.class, MODID + ":enderhopper");
    }


    private void modelRegister(){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
        ModelLoader.setCustomStateMapper(
                this, (new StateMap.Builder()).ignore(BlockHopper.ENABLED).build()
        );
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBucket();
    }
}
