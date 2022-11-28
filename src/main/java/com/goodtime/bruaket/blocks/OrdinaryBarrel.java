package com.goodtime.bruaket.blocks;

import com.goodtime.bruaket.entity.TileEntityOrdinaryBarrel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class OrdinaryBarrel extends Barrel{

    static {
        GameRegistry.registerTileEntity(TileEntityOrdinaryBarrel.class, new ResourceLocation("bruaket:barrel"));
    }

    public OrdinaryBarrel(String registerName, float hardness) {
        super(registerName, hardness);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityOrdinaryBarrel(this.getRegistryName());
    }

}
