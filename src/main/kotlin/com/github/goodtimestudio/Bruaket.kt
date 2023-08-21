/*
 * This file is part of Bruaket, licensed under the MIT License (MIT).
 * Copyright (c) GoodTime Studio 2023.
 */

package com.github.goodtimestudio

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Bruaket : ModInitializer {
    private val logger = LoggerFactory.getLogger("bruaket")

	override fun onInitialize() {
		logger.info("Hello Fabric world!")
	}
}