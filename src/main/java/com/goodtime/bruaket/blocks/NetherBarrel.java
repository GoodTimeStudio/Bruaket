package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.entity.TileEntityNetherBarrel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NetherBarrel extends Barrel{

    static {
        GameRegistry.registerTileEntity(TileEntityNetherBarrel.class, new ResourceLocation("bruaket:barrel"));
    }
    public NetherBarrel(String registerName, float hardness) {
        super(registerName, hardness);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNetherBarrel(this.getRegistryName());
    }

}
