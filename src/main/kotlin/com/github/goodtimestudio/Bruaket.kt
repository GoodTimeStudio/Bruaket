/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) 2023. GoodTime Studio
 */

package com.github.goodtimestudio

import net.fabricmc.api.ModInitializer
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object Bruaket : ModInitializer {
	val MOD_ID = "桶 Bruaket"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOGGER.info("正在加载桶模组！ Loading Bruaket mod !")
		LOGGER.info("加载完成！ Loading completed !")
	}
}