package com.goodtime.bruaket.config;

import com.goodtime.bruaket.core.Bruaket;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Bruaket.MODID)
@Config.LangKey("config.bruaket.general")
@Mod.EventBusSubscriber(modid = Bruaket.MODID)
public final class BruaketConfig {


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Bruaket.MODID)) {
            ConfigManager.sync(Bruaket.MODID, Config.Type.INSTANCE);
        }
    }

    @Config.Comment("This value will be multiplied by the recipe time configured in the CraftTweaker configuration file to obtain the recipe synthesis time")
    @Config.LangKey("config.bruaket.general.recipe_time_multiplier")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresWorldRestart
    public static int recipeTimeMultiplier = 1;

    @Config.Comment("This value will be multiplied by the total consumption set in SMelting talisman to obtain the specific consumption of that talisman")
    @Config.LangKey("config.bruaket.general.smelting_consumption_multiplier")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresWorldRestart
    public static int smeltingConsumptionMultiplier = 1;


    @Mod.EventBusSubscriber(modid = Bruaket.MODID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Bruaket.MODID)) {
                ConfigManager.sync(Bruaket.MODID, Config.Type.INSTANCE);
            }
        }
    }


}
