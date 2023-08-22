/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio.init

import com.github.goodtimestudio.Bruaket
import com.github.goodtimestudio.items.Items
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents.Register
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class InitItems(itemMap: HashMap<String, Item>) {
    private val ITEM_MAP = itemMap

    private fun addItemToMap(name: String, item: Item) {
        ITEM_MAP[name] = item
    }

    fun register() {
        ITEM_MAP.forEach { (name, item) ->
            Bruaket.LOGGER.info("Registering for Item $name")
            Registry.register(Registries.ITEM, Identifier(Bruaket.MOD_ID, name), item)
        }
    }

    init {
        addItemToMap("bruaket.item.stone_talisman",Items().STONE_TALISMAN)
        addItemToMap("bruaket.item.fire_talisman",Items().FIRE_TALISMAN)
        addItemToMap("bruaket.item.wood_talisman",Items().WOOD_TALISMAN)
        addItemToMap("bruaket.item.iron_talisman",Items().IRON_TALISMAN)
        addItemToMap("bruaket.item.water_talisman",Items().WATER_TALISMAN)
        addItemToMap("bruaket.item.ultra_flamma_talisman",Items().ULTRA_Flamma_TALISMAN)
        addItemToMap("bruaket.item.maxima_talisman",Items().MAXIMA_Flamma_TALISMAN)
        addItemToMap("bruaket.item.barrel_seed",Items().BARREL_SEED)
        //addItemToMap("bruaket.item.barrel_patern",Items().BARREL_PATTERN)
    }
}